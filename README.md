# JWT Authentication - Spring Boot

## Overview

This project demonstrates JWT (JSON Web Token) based authentication implemented with **Spring Boot 3**, **Spring Security 6**, and **JJWT** (Java JWT library). It provides a stateless, token-based authentication system with user registration, login, and access to protected routes.

---

## Project Structure

```
FS exp6/
├── src/
│   └── main/
│       ├── java/com/jwtauth/
│       │   ├── JwtAuthApplication.java          ← Main Spring Boot entry point
│       │   ├── config/
│       │   │   └── SecurityConfig.java          ← Spring Security + JWT configuration
│       │   ├── controller/
│       │   │   ├── AuthController.java          ← /api/auth/** (register, login, logout)
│       │   │   ├── ProtectedController.java     ← /api/protected/** (JWT-protected routes)
│       │   │   └── PublicController.java        ← /api/public/** (no auth needed)
│       │   ├── dto/
│       │   │   ├── AuthResponse.java            ← JWT token response DTO
│       │   │   ├── LoginRequest.java            ← Login body DTO
│       │   │   └── RegisterRequest.java         ← Register body DTO
│       │   ├── model/
│       │   │   └── User.java                    ← JPA User entity
│       │   ├── repository/
│       │   │   └── UserRepository.java          ← Spring Data JPA repository
│       │   ├── security/
│       │   │   ├── JwtAuthenticationFilter.java ← Intercepts requests & validates JWT
│       │   │   ├── JwtUtil.java                 ← Token generation & validation logic
│       │   │   └── UserDetailsServiceImpl.java  ← Loads user from DB for Spring Security
│       │   └── service/
│       │       └── AuthService.java             ← Business logic for auth operations
│       └── resources/
│           └── application.properties           ← App config (DB, JWT secret, port)
├── screenshots/                                 ← Postman demo screenshots
│   ├── 01_register_user.png
│   ├── 02_login_get_token.png
│   ├── 03_access_protected_route.png
│   ├── 04_access_without_token_401.png
│   └── 05_logout.png
└── pom.xml                                      ← Maven build file
```

---

## Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Spring Boot | 3.2.3 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| JJWT | 0.11.5 | JWT token generation/validation |
| Spring Data JPA | 3.x | Database ORM |
| H2 Database | In-memory | Development database |
| Lombok | Latest | Boilerplate reduction |
| Java | 17 | Language |

---

## API Endpoints

### Public Endpoints (No Authentication Required)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |
| POST | `/api/auth/logout` | Logout (client discards token) |
| GET | `/api/public/health` | Health check |

### Protected Endpoints (JWT Required)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/protected/dashboard` | User dashboard (any authenticated user) |
| GET | `/api/protected/profile` | Current user's profile |
| GET | `/api/protected/admin` | Admin panel (ROLE_ADMIN only) |

---

## How JWT Authentication Works

```
┌──────────┐                    ┌─────────────┐              ┌──────────┐
│  Client  │                    │   Server    │              │    DB    │
└────┬─────┘                    └──────┬──────┘              └────┬─────┘
     │   POST /api/auth/login          │                          │
     │   { username, password }        │                          │
     │ ──────────────────────────────► │                          │
     │                                 │  Find user by username   │
     │                                 │ ────────────────────────►│
     │                                 │  Return user + hash      │
     │                                 │ ◄────────────────────────│
     │                                 │  Verify password hash    │
     │                                 │  Generate JWT token      │
     │   { token: "ey..." }            │                          │
     │ ◄───────────────────────────────│                          │
     │                                 │                          │
     │   GET /api/protected/dashboard  │                          │
     │   Authorization: Bearer ey...   │                          │
     │ ──────────────────────────────► │                          │
     │                                 │  JwtAuthenticationFilter │
     │                                 │  ├─ Extract token        │
     │                                 │  ├─ Validate signature   │
     │                                 │  ├─ Check expiration     │
     │                                 │  └─ Set SecurityContext  │
     │   200 OK { dashboard data }     │                          │
     │ ◄───────────────────────────────│                          │
```

### Flow Explanation

1. **Registration** (`POST /api/auth/register`): User provides username, password, email. Password is hashed with **BCrypt** and stored in DB. A JWT token is returned immediately.

2. **Login** (`POST /api/auth/login`): Spring Security's `AuthenticationManager` verifies credentials. On success, a signed **JWT token** is generated using `JwtUtil` and returned to the client.

3. **Accessing Protected Routes**: The client sends the token in the `Authorization` header as `Bearer <token>`. The `JwtAuthenticationFilter` intercepts every request:
   - Extracts the token
   - Validates signature & expiry using `JwtUtil`
   - Loads user details and sets authentication in the `SecurityContextHolder`
   - Spring Security grants/denies access based on route rules

4. **Logout**: JWT is stateless — the server holds no session. Logout is handled client-side by discarding the token. For production, add tokens to a Redis blacklist.

---

## JWT Token Structure

A JWT consists of three Base64-encoded parts:

```
eyJhbGciOiJIUzI1NiJ9   ← Header (algorithm: HS256)
.
eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzA5Nzc0ODAwLCJleHAiOjE3MDk4NjEyMDB9
                        ← Payload (sub: username, iat: issued-at, exp: expiry)
.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
                        ← Signature (HMAC-SHA256 with secret key)
```

---

## Running the Application

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
# Clone / navigate to project
cd "FS exp6"

# Build and run
mvn spring-boot:run
```

The server starts on **http://localhost:8080**.

---

## Postman Demonstration

### Step 1 – Register a New User

**Request:**
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123",
  "email": "john@example.com"
}
```

**Response:** `200 OK` with JWT token in response body.

---

### Step 2 – Login and Receive Token

**Request:**
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "johndoe",
  "message": "Authentication successful"
}
```

---

### Step 3 – Access Protected Route

**Request:**
```
GET http://localhost:8080/api/protected/dashboard
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Response:** `200 OK` with dashboard data.

---

### Step 4 – Access Without Token (Should Fail)

**Request:**
```
GET http://localhost:8080/api/protected/dashboard
(No Authorization header)
```

**Response:** `403 Forbidden`

---

### Step 5 – Logout

**Request:**
```
POST http://localhost:8080/api/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Response:**
```json
{
  "message": "Logout successful. Please discard your token on the client side.",
  "status": "success"
}
```

---

## Security Configuration

- **Stateless sessions**: Spring Security is configured with `SessionCreationPolicy.STATELESS` — no HTTP sessions are created.
- **BCrypt password hashing**: All passwords are hashed with `BCryptPasswordEncoder` (strength 10).
- **JWT signing**: Tokens are signed with **HMAC-SHA256** using a 256-bit secret key.
- **Token expiry**: 24 hours (86,400,000 ms), configured in `application.properties`.
- **Role-based access**: `@PreAuthorize("hasRole('ADMIN')")` guards admin-only routes.

---

## Screenshots

Screenshots demonstrating the system can be found in the `screenshots/` folder:

| File | Description |
|---|---|
| `01_register_user.png` | Successfully registering a new user and receiving a JWT token |
| `02_login_get_token.png` | Logging in with credentials and receiving a JWT token |
| `03_access_protected_route.png` | Accessing a protected route with a valid JWT token |
| `04_access_without_token_401.png` | Attempting to access a protected route without a token (403 response) |
| `05_logout.png` | Logout endpoint response |
