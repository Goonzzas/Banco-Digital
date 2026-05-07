# 🚀 Guía de Despliegue: Banco Digital (Producción)

Esta guía contiene todo lo necesario para que el sistema funcione en **Render** (Backend), **Vercel** (Frontend) y se conecte a **Supabase**.

---

## 🛠️ Configuración del Backend (Render)

Tu compañero debe crear un nuevo "Web Service" en Render y configurar las siguientes **Environment Variables**:

### 📊 Base de Datos (Supabase)
| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | URL JDBC de Supabase | `jdbc:postgresql://db.xxxx.supabase.co:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la DB | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la DB | `tu_password_de_supabase` |
| `SPRING_DATASOURCE_DRIVER` | Driver de Postgres | `org.postgresql.Driver` |
| `SPRING_JPA_PLATFORM` | Dialecto de Hibernate | `org.hibernate.dialect.PostgreSQLDialect` |

### 🔐 Seguridad y URLs
| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| `JWT_SECRET` | Firma para los tokens | `usa_un_string_muy_largo_y_aleatorio` |
| `FRONTEND_URL` | URL donde estará el Frontend | `https://banco-digital.vercel.app` |

### 📧 Correo Electrónico (Gmail SMTP)
| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| `MAIL_USERNAME` | Tu correo de Gmail | `tu_banco@gmail.com` |
| `MAIL_PASSWORD` | Contraseña de Aplicación | `xxxx xxxx xxxx xxxx` |

> [!IMPORTANT]
> **Comandos de Despliegue en Render:**
> - **Build Command:** `./mvnw clean install -DskipTests`
> - **Start Command:** `java -jar target/api-0.0.1-SNAPSHOT.jar`

---

## 💻 Configuración del Frontend (Vercel)

En el panel de Vercel, se deben añadir estas variables de entorno:

| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| `VITE_API_URL` | URL de la API en Render | `https://api-banco.onrender.com/api` |
| `VITE_UPLOADS_URL` | URL para ver imágenes | `https://api-banco.onrender.com/uploads` |

---

## 📝 Notas Finales

> [!TIP]
> **Base de Datos:** No hace falta crear las tablas manualmente. Al conectar Supabase por primera vez, el sistema las creará automáticamente gracias a `spring.jpa.hibernate.ddl-auto=update`.

> [!WARNING]
> **MFA (OTP):** Los correos solo se enviarán de verdad si las variables `MAIL_USERNAME` y `MAIL_PASSWORD` son correctas. De lo contrario, el código OTP se imprimirá en los logs de Render para pruebas.

> [!NOTE]
> El sistema local sigue funcionando perfectamente con H2 y localhost por defecto. Esta configuración externa solo se activa cuando se detectan las variables de entorno arriba mencionadas.
