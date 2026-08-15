# Multi-Vendor Marketplace Platform

A production-ready multi-vendor e-commerce platform built with Spring Boot microservices and Next.js frontend.

## Features

### Customer Features
- Product browsing and search
- Shopping cart management
- Wishlist functionality
- Order placement and tracking
- User profile management

### Vendor Features
- Vendor registration and dashboard
- Product management (CRUD)
- Order management
- Sales analytics
- Earnings tracking

### Admin Features
- Dashboard with metrics
- Vendor management (approve/reject/suspend)
- Product moderation
- Commission rule management
- User management
- Report generation

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.2.5
- Spring Cloud Gateway
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0
- Redis 7
- Apache Kafka 7.5
- Flyway (Database Migration)
- Maven

### Frontend
- Next.js 14
- React 18
- TypeScript
- Tailwind CSS
- React Query
- React Hook Form

### Infrastructure
- Docker & Docker Compose
- Nginx (Production)
- Prometheus (Metrics)
- Spring Actuator (Health Checks)

## Quick Start

### Prerequisites
- Java 21
- Node.js 20+
- Docker & Docker Compose
- Maven 3.9+

### Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd marketplace
   ```

2. **Start infrastructure**
   ```bash
   docker-compose -f docker-compose.dev.yml up -d
   ```

3. **Build shared library**
   ```bash
   cd shared-library
   mvn clean install
   ```

4. **Build all services**
   ```bash
   cd ..
   mvn clean package -DskipTests
   ```

5. **Start services**
   ```bash
   # Start each service in separate terminals
   java -jar marketplace-auth/target/marketplace-auth-1.0.0-SNAPSHOT-exec.jar
   java -jar marketplace-vendor/target/marketplace-vendor-1.0.0-SNAPSHOT-exec.jar
   # ... repeat for other services
   ```

6. **Start frontend**
   ```bash
   cd marketplace-frontend
   npm install
   npm run dev
   ```

### Docker Setup

1. **Build all services**
   ```bash
   mvn clean package -DskipTests
   cd marketplace-frontend && npm run build && cd ..
   ```

2. **Start all services**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - Frontend: http://localhost:3000
   - API Gateway: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

## API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register customer
- `POST /api/v1/auth/register/vendor` - Register vendor
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/refresh` - Refresh token
- `POST /api/v1/auth/logout` - Logout

### Products
- `GET /api/v1/products` - List products
- `GET /api/v1/products/{id}` - Get product
- `POST /api/v1/products` - Create product (Vendor)
- `PUT /api/v1/products/{id}` - Update product (Vendor)
- `DELETE /api/v1/products/{id}` - Delete product (Vendor)

### Cart
- `GET /api/v1/cart` - Get cart
- `POST /api/v1/cart/items` - Add item
- `PUT /api/v1/cart/items` - Update item
- `DELETE /api/v1/cart/items/{id}` - Remove item

### Orders
- `GET /api/v1/orders` - List orders
- `POST /api/v1/orders/checkout` - Checkout
- `GET /api/v1/orders/{id}` - Get order

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_ROOT_PASSWORD` | MySQL root password | root |
| `DB_NAME` | Database name | multivendor_marketplace |
| `DB_USERNAME` | Database username | marketplace |
| `DB_PASSWORD` | Database password | marketplace |
| `JWT_SECRET` | JWT signing key | your-super-secret-jwt-key... |
| `REDIS_HOST` | Redis host | localhost |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka brokers | localhost:9092 |

### Profiles
- `dev` - Development configuration
- `staging` - Staging configuration
- `prod` - Production configuration

## Health Checks

- Gateway: `http://localhost:8080/actuator/health`
- Auth Service: `http://localhost:8081/actuator/health`
- Frontend: `http://localhost:3000`

## License

MIT License
