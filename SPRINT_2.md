# 🏦 Banco Digital - Sprint 2

¡Bienvenido al **Sprint 2** del proyecto Banco Digital! En esta etapa, hemos transformado la experiencia del usuario, pasando de una plataforma básica a un sistema financiero dinámico y funcional.

---

## 🚀 Nuevas Funcionalidades

### 📈 Historial de Movimientos (Movements)
Hemos implementado una vista dedicada para que los usuarios puedan rastrear cada centavo.
- **Visualización Clara**: Diferenciación visual entre entradas (verde) y salidas (negro/rojo).
- **Detalles de Cuenta**: Identificación de si el movimiento fue desde una cuenta de **Ahorros** o **Corriente**.
- **Enmascaramiento de Seguridad**: Las cuentas de origen y destino se muestran enmascaradas (ej. `****1234`) para proteger la privacidad.

### 🔍 Filtros Inteligentes
¡No más búsquedas infinitas! Ahora el usuario puede:
- **Filtrar por Fecha**: Seleccionar un rango de fechas (`Desde` - `Hasta`) para encontrar transacciones específicas.
- **Búsqueda en Tiempo Real**: Los resultados se actualizan instantáneamente al aplicar los filtros.

### 📄 Comprobantes Digitales (Receipts)
Cada transacción genera un comprobante profesional y estético.
- **Modal de Detalle**: Al hacer clic en un movimiento, se despliega un "ticket" con:
  - Número de referencia único.
  - Fecha y hora exacta.
  - Monto destacado.
  - Estado de la transacción (Confirmado/Exitoso).
- **Diseño Premium**: Uso de efectos de desenfoque (backdrop-blur) y sombras suaves.

---

## 🎨 Mejoras en UI/UX

Hemos elevado el estándar visual de la aplicación:
- **Glassmorphism**: Menús y tarjetas con efectos de transparencia y desenfoque.
- **Micro-animaciones**: Transiciones suaves al abrir modales y cargar datos.
- **Arquitectura Responsiva**: La interfaz se adapta perfectamente a diferentes tamaños de pantalla.
- **Iconografía Moderna**: Integración de `Lucide-React` para una navegación intuitiva.

---

## ⚙️ Cambios Técnicos (Backend)

- **DTOs Optimizados**: Se creó `TransactionDTO` para enviar solo la información necesaria y formateada al frontend.
- **Lógica de Auditoría**: Cada transferencia exitosa o fallida se registra automáticamente en el sistema de auditoría.
- **Consultas Personalizadas**: Implementación de queries en JPA para soportar el filtrado por rango de fechas y IDs de cuenta.

---

## 🛠️ Cómo Probar

1. Inicia sesión en la plataforma.
2. Dirígete a la sección de **Movimientos**.
3. Realiza una transferencia y observa cómo aparece instantáneamente en tu historial.
4. Usa los selectores de fecha para filtrar tus gastos del mes.
5. Haz clic en el ícono de documento para ver tu comprobante.

---

> **Nota para el equipo:** Esta versión sienta las bases para las futuras integraciones de servicios y pagos programados en el Sprint 3.
