package com.banco.digital.services;

import com.banco.digital.models.Account;
import com.banco.digital.models.Transaction;
import com.banco.digital.repositories.AccountRepository;
import com.banco.digital.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final AuditService auditService;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthService authService, AuditService auditService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.authService = authService;
        this.auditService = auditService;
    }

    @Transactional
    public void transfer(String fromNum, String toNum, java.math.BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findByAccountNumber(fromNum)
                .orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));
        

        Account toAccount = accountRepository.findByAccountNumber(toNum)
                .orElseThrow(() -> new RuntimeException("Cuenta de destino no encontrada"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            auditService.logAction(fromAccount.getUser().getEmail(), "TRANSFER_FAILED", "FAILED", "Saldo insuficiente: " + amount, "system");
            throw new RuntimeException("Saldo insuficiente");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .fromAccountId(fromAccount.getId())
                .toAccountId(toAccount.getId())
                .amount(amount)
                .type("TRANSFER")
                .description(description)
                .referenceNumber("TR-" + System.currentTimeMillis())
                .build();
        
        transactionRepository.save(transaction);
        
        auditService.logAction(fromAccount.getUser().getEmail(), "TRANSFER_SUCCESS", "SUCCESS", 
            String.format("Transferencia de %s a cuenta %s", amount, toNum), "system");
    }

    public List<com.banco.digital.dto.TransactionDTO> getHistory(Long userId, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        List<Long> ids = accounts.stream().map(Account::getId).collect(Collectors.toList());
        if (ids.isEmpty()) return java.util.Collections.emptyList();
        
        List<Transaction> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByAccountIdsAndDateRange(ids, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        } else {
            transactions = transactionRepository.findByAccountIds(ids);
        }
        
        java.util.List<com.banco.digital.dto.TransactionDTO> result = new java.util.ArrayList<>();
        
        for (Transaction t : transactions) {
            boolean isFromMe = t.getFromAccountId() != null && ids.contains(t.getFromAccountId());
            boolean isToMe = t.getToAccountId() != null && ids.contains(t.getToAccountId());
            
            // Si es salida (o transferencia entre mis cuentas, mostramos la salida)
            if (isFromMe) {
                result.add(mapToDTO(t, true));
            }
            
            // Si es entrada (o transferencia entre mis cuentas, mostramos la entrada)
            if (isToMe) {
                result.add(mapToDTO(t, false));
            }
        }
        return result;
    }

    private com.banco.digital.dto.TransactionDTO mapToDTO(Transaction t, boolean asOutgoing) {
        String fromMasked = "N/A";
        if (t.getFromAccountId() != null) {
            fromMasked = accountRepository.findById(t.getFromAccountId())
                    .map(acc -> maskAccountNumber(acc.getAccountNumber()))
                    .orElse("Unknown");
        }
        
        String toMasked = "N/A";
        if (t.getToAccountId() != null) {
            toMasked = accountRepository.findById(t.getToAccountId())
                    .map(acc -> maskAccountNumber(acc.getAccountNumber()))
                    .orElse("Unknown");
        }

        String accType = "N/A";
        Long targetAccId = asOutgoing ? t.getFromAccountId() : t.getToAccountId();
        if (targetAccId != null) {
            accType = accountRepository.findById(targetAccId)
                    .map(Account::getType)
                    .orElse("N/A");
        }

        return com.banco.digital.dto.TransactionDTO.builder()
                .id(t.getId())
                .fromAccountMasked(fromMasked)
                .toAccountMasked(toMasked)
                .amount(t.getAmount())
                .type(t.getType())
                .description(t.getDescription() + (asOutgoing ? " (Enviado)" : " (Recibido)"))
                .referenceNumber(t.getReferenceNumber())
                .accountType(accType)
                .isOutgoing(asOutgoing) // NECESITAMOS ESTO EN EL DTO
                .createdAt(t.getCreatedAt())
                .build();
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
}
