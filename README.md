# 🏦 Srinu Bankers - Loan Management Backend

A robust, production-ready Spring Boot REST API for managing gold and personal loans, customer records, payment tracking, automated expiry reminders, and secure authentication.

---

## 🚀 Tech Stack

- **Framework:** Spring Boot 4.x / Spring Framework 7
- **Language:** Java 17+
- **Security:** Spring Security, JWT (JSON Web Tokens via JJWT), BCrypt Password Hashing
- **Persistence:** Spring Data JPA, Hibernate, MySQL Connector/J
- **Database:** MySQL
- **Email Service:** Spring Mail (JavaMailSender / Gmail SMTP with SSL/TLS)
- **Containerization:** Docker (Multi-stage build)
- **Hosting / Deployment:** Render

---

## ✨ Features

- **🔐 Secure Authentication:**
  - Phone & password-based registration and login.
  - Password hashing with **BCrypt**.
  - **Zero-Downtime Migration:** Seamless auto-upgrade of legacy plaintext passwords to BCrypt on first login.
  - Stateless **JWT (JSON Web Token)** authorization with 24-hour expiration.
- **📄 Loan Lifecycle Management:**
  - Create, search, edit, close, and delete loans.
  - Automatic status tracking (`Active`, `Closed`).
  - Records borrower details, phone, email, collateral item, weight, amount, interest rate, date, and repayment amount.
- **⏰ Loan Expiry Tracking & Automated Email Reminders:**
  - Automatically identifies loans nearing or exceeding their 11-month tenure.
  - Asynchronous email confirmation on loan creation.
  - Automated loan reminder emails sent directly to borrowers.
- **🌐 Cross-Origin (CORS) Support:**
  - Configured for cross-platform clients (React, Angular, Vue, Flutter, React Native, Mobile Apps).

---

## 📁 Project Structure

```
MyApp-backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/loanbackend/
│   │   │   ├── controller/          # REST API endpoints (AuthController, LoanController)
│   │   │   ├── dto/                 # Data transfer objects (AuthRequest, AuthResponse)
│   │   │   ├── model/               # JPA entities (User, Loan)
│   │   │   ├── repository/          # Spring Data JPA repositories (UserRepository, LoanRepository)
│   │   │   ├── security/            # Spring Security, JWT filter, JwtUtils, UserDetailsService
│   │   │   ├── service/             # Business logic & email services (EmailService)
│   │   │   └── LoanBackendApplication.java
│   │   └── resources/
│   │       └── application.properties # Application configurations
│   └── test/
│       └── java/com/example/loanbackend/ # Automated unit tests (AuthControllerTest, JwtUtilsTest)
├── Dockerfile                       # Multi-stage production container build
├── .dockerignore                    # Build exclusions for Docker
├── pom.xml                          # Maven build & dependencies configuration
├── run.ps1                          # PowerShell startup script with .env loader
└── README.md                        # Documentation
```

---

## ⚙️ Environment Variables

The backend requires the following environment variables (defined in your `.env` file locally or in your Render environment):

| Variable | Description | Example |
| :--- | :--- | :--- |
| `DB_URL` | JDBC MySQL connection URL | `jdbc:mysql://localhost:3306/loan_db` |
| `DB_USERNAME` | MySQL database username | `root` |
| `DB_PASSWORD` | MySQL database password | `your_db_password` |
| `MAIL_USERNAME` | Gmail address for sending alerts | `your_email@gmail.com` |
| `MAIL_PASSWORD` | Gmail App Password (16-character) | `xxxx xxxx xxxx xxxx` |
| `JWT_SECRET` | *(Optional)* Secret key for signing JWTs | `404E635266556A586E3272357538...` |

---

## 🛠️ Local Setup & Running

### 1. Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher
- **MySQL Database Server:** Running locally or remotely

### 2. Configure `.env`
Create a `.env` file in the `MyApp-backend` root folder:
```properties
DB_URL=jdbc:mysql://localhost:3306/loan_db
DB_USERNAME=root
DB_PASSWORD=your_password
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

### 3. Run Locally
**Option A: Using PowerShell runner (automatically loads `.env`):**
```powershell
.\run.ps1
```

**Option B: Using Maven Wrapper directly:**
```powershell
.\mvnw.cmd spring-boot:run
```

---

## 🐳 Running with Docker

### 1. Build the Docker Image
```powershell
docker build -t myapp-backend .
```

### 2. Run the Container
```powershell
docker run -d -p 8080:8080 --env-file .env --name myapp-backend-container myapp-backend
```

> **Note:** If MySQL is running on your host machine outside Docker, set `DB_URL=jdbc:mysql://host.docker.internal:3306/loan_db`.

---

## 📡 API Endpoints

### 🔑 Authentication (`/api/auth`)
*Public endpoints - no token required.*

#### 1. Register User
- **Method:** `POST /api/auth/register`
- **Body:**
  ```json
  {
    "phone": "9876543210",
    "password": "securePassword123"
  }
  ```
- **Response:** `"Registered successfully ✅"`

#### 2. Login
- **Method:** `POST /api/auth/login`
- **Body:**
  ```json
  {
    "phone": "9876543210",
    "password": "securePassword123"
  }
  ```
- **Response:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "phone": "9876543210",
    "message": "Login successful"
  }
  ```

---

### 💰 Loans (`/api/loans`)
*Protected endpoints - require `Authorization: Bearer <token>` header.*

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/loans` | Create a new loan (sends confirmation email) |
| `GET` | `/api/loans` | Retrieve all loans |
| `GET` | `/api/loans/search?name={name}` | Search active loans by customer name |
| `PUT` | `/api/loans/edit/{id}` | Update borrower and loan details |
| `PUT` | `/api/loans/close/{id}` | Close a loan (records repayment date & amount) |
| `DELETE` | `/api/loans/{id}` | Delete a loan record |
| `GET` | `/api/loans/expiring` | Get all active loans older than 11 months |
| `GET` | `/api/loans/send-reminder/{id}` | Trigger an expiry reminder email to the borrower |

#### Example Loan Payload (`POST /api/loans`)
```json
{
  "name": "John Doe",
  "phone": "9876543210",
  "email": "johndoe@example.com",
  "address": "123 Main St, City",
  "item": "Gold Chain",
  "weight": "15g",
  "amount": "50000",
  "interest": "2%",
  "date": "2026-04-15"
}
```

---

## 🧪 Running Automated Tests

Run the full test suite using Maven:
```powershell
.\mvnw.cmd test "-Dtest=JwtUtilsTest,AuthControllerTest"
```

The test suite validates:
- JWT creation, validation, claim extraction, and rejection of invalid tokens.
- User registration with BCrypt password encryption.
- Verification of BCrypt hashed passwords.
- **Legacy user auto-upgrade:** Verifies plaintext passwords from previous production versions are checked, authenticated, and transparently converted to BCrypt in the database.

---

## 🚀 Production Deployment (Render)

1. Commit and merge changes to `main`:
   ```powershell
   git checkout main
   git merge feature/spring-security
   git push origin main
   ```
2. Render detects the push, builds the container via [Dockerfile](Dockerfile), sets up environment variables configured in your Render dashboard, and deploys the service.

---

## 📄 License
Internal proprietary application for **Srinu Bankers**.
