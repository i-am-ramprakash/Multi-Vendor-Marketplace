# Multi-Vendor Marketplace - Authentication Module

A production-ready authentication and authorization module for a Multi-Vendor Marketplace platform built with Java 21, Spring Boot 3, and Spring Security 6.

## Architecture

This module follows **Hexagonal Architecture** (Ports and Adapters) pattern:

```
marketplace-auth/
├── src/main/java/com/marketplace/auth/
│   ├── api/                    # Controllers, exception handlers, Swagger
│   ├── application/            # Use cases, DTOs, application services
│   ├── domain/                 # Entities, value objects, events, repository interfaces
│   └── infrastructure/         # JPA repositories, JWT, security config, email
├── src/main/resources/
│   ├── application.yml         # Application configuration
│   └── db/migration/           # Flyway migrations
└── src/test/                   # Unit and integration tests
```

## Features

### JWT Authentication & Authorization
- User registration (Customer & Vendor)
- JWT access and refresh tokens
- Role-based access control (ADMIN, VENDOR, CUSTOMER)
- Password encryption with BCrypt
- Email verification
- Password reset flow
- Token refresh and logout

### Security
- Stateless session management
- CORS configuration
- Rate limiting ready
- Structured logging
- Input validation

### Database
- MySQL with JPA/Hibernate
- Flyway migrations
- Optimistic locking
- Connection pooling with HikariCP

### API Documentation
- OpenAPI 3.0 (Swagger)
- Request/Response DTOs
- Error handling

### DevOps
- Docker & Docker Compose
- GitHub Actions CI/CD
- JaCoCo code coverage
- Testcontainers support

## Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8.0+
- Docker & Docker Compose (optional)

## Quick Start

### Using Docker Compose

```bash
# Start all services
docker compose up -d

# Check logs
docker compose logs -f auth-service
```

### Manual Setup

1. **Start MySQL**
   ```bash
   docker run -d --name mysql \
     -e MYSQL_ROOT_PASSWORD=password \
     -e MYSQL_DATABASE=multivendor_marketplace \
     -p 3306:3306 mysql:8.0
   ```

2. **Set environment variables**
   ```bash
   export DB_USERNAME=root
   export DB_PASSWORD=password
   export JWT_SECRET=your-super-secret-jwt-key-at-least-256-bits-long
   ```

3. **Build and run**
   ```bash
   mvn clean package -DskipTests
   java -jar target/marketplace-auth-1.0.0-SNAPSHOT.jar
   ```

## API Endpoints

### Authentication
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/v1/auth/register` | Register customer | Public |
| POST | `/api/v1/auth/register/vendor` | Register vendor | Public |
| POST | `/api/v1/auth/login` | Login | Public |
| POST | `/api/v1/auth/refresh` | Refresh token | Public |
| POST | `/api/v1/auth/logout` | Logout | Authenticated |
| POST | `/api/v1/auth/change-password` | Change password | Authenticated |
| POST | `/api/v1/auth/forgot-password` | Request password reset | Public |
| POST | `/api/v1/auth/reset-password` | Reset password | Public |
| POST | `/api/v1/auth/verify-email` | Verify email | Public |
| POST | `/api/v1/auth/resend-verification` | Resend verification email | Public |
| GET | `/api/v1/auth/me` | Get profile | Authenticated |
| PUT | `/api/v1/auth/me` | Update profile | Authenticated |

### Swagger UI
Access the API documentation at: `http://localhost:8080/swagger-ui.html`

## Configuration

### Application Properties

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/multivendor_marketplace
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}

app:
  jwt:
    secret: ${JWT_SECRET:your-secret-key}
    access-token-expiry-minutes: 60
    refresh-token-expiry-days: 30
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_USERNAME` | Database username | root |
| `DB_PASSWORD` | Database password | password |
| `JWT_SECRET` | JWT signing secret | (required) |
| `MAIL_HOST` | SMTP server host | localhost |
| `MAIL_PORT` | SMTP server port | 587 |
| `FRONTEND_URL` | Frontend application URL | http://localhost:3000 |

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run with coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## Development

### Adding New Use Cases

1. Create DTO in `application/dto`
2. Create use case in `application/usecase`
3. Implement service interface in `application/service`
4. Add controller method in `api/controller`

### Domain Events

Domain events are published using Spring's `ApplicationEventPublisher`:
- `UserRegisteredEvent`
- `UserLoggedInEvent`
- `PasswordChangedEvent`
- `EmailVerifiedEvent`

## Database Schema

### Tables
- `users` - User accounts
- `roles` - User roles (ADMIN, VENDOR, CUSTOMER)
- `user_roles` - User-role mapping
- `refresh_tokens` - JWT refresh tokens

### Flyway Migrations
Migrations are located in `src/main/resources/db/migration/`

## License

This project is licensed under the Apache License 2.0.