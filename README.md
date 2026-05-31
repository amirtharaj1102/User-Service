# User Service - Setup and Run

## Quick Start (First-Time Setup)

1. Install Node.js (v18+). Download from https://nodejs.org/en/download/.
2. Install frontend dependencies:
   ```bash
   cd ../CodeRank
   npm install --legacy-peer-deps
   ```
3. Return to the backend and proceed with database + service steps below.

---

## 💾 1. Database Setup

CodeRank uses PostgreSQL for user account persistence.

1. Open your PostgreSQL terminal (`psql`) or a database management client (e.g., pgAdmin, DBeaver).
2. Connect with admin privileges and run:
   ```sql
   CREATE DATABASE coderank_users;
   CREATE DATABASE coderank_code;
   ```
3. The local configuration defaults to:
   - **Host**: `localhost:5432`
   - **Database**: `coderank_users`
   - **Username**: `postgres`
   - **Password**: `postgres`

---

## ☕ 2. User Service (Spring Boot Backend)

The service is pre-configured with active environment profiles for local development and production.

### Profiles Configuration
- **Development (`application-dev.yaml`)**:
  Connects to PostgreSQL at `localhost:5432/coderank_users` with hardcoded credentials (`postgres`/`postgres`).
- **Production (`application-prod.yaml`)**:
  Pulls credentials dynamically from the environment. You must set the following environment variables:
  ```powershell
  $env:DB_HOST="production-db-url"
  $env:DB_PORT="5432"
  $env:DB_NAME="coderank_users"
  $env:DB_USER="prod_user"
  $env:DB_PASSWORD="securepassword"
  $env:JWT_SECRET="your-super-secret-hmac-sha256-string-at-least-256-bits"
  ```

### Run the Backend Service
From the root of the project, navigate to the `User Service` folder and execute the Maven Wrapper:

```bash
cd "User Service"
# On Windows PowerShell:
./mvnw.cmd spring-boot:run

# On Linux/macOS:
./mvnw spring-boot:run
```
The server will start listening on port **`8080`**.
