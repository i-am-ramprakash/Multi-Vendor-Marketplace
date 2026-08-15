# Multi-Vendor Marketplace Architecture

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                          CLIENTS                                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐             │
│  │   Web App    │  │  Mobile App  │  │  Admin Panel │             │
│  │  (Next.js)   │  │   (Future)   │  │  (Next.js)   │             │
│  └──────────────┘  └──────────────┘  └──────────────┘             │
└─────────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    API GATEWAY (Port 8080)                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Spring Cloud Gateway                                       │   │
│  │  • JWT Authentication Filter                                │   │
│  │  • Rate Limiting Filter                                     │   │
│  │  • Request Logging Filter                                   │   │
│  │  • CORS Configuration                                       │   │
│  │  • Circuit Breaker                                          │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  Route Configuration:                                               │
│  /api/v1/auth/**      → marketplace-auth (8081)                    │
│  /api/v1/vendors/**   → marketplace-vendor (8082)                  │
│  /api/v1/products/**  → marketplace-product (8083)                 │
│  /api/v1/cart/**      → marketplace-cart (8084)                    │
│  /api/v1/wishlist/**  → marketplace-wishlist (8085)                │
│  /api/v1/orders/**    → marketplace-order (8086)                   │
│  /api/v1/commissions/** → marketplace-commission (8087)            │
│  /api/v1/notifications/** → marketplace-notification (8088)        │
│  /api/v1/urls/**      → marketplace-url-shortener (8089)          │
│  /api/v1/cache/**     → marketplace-cache (8090)                   │
│  /api/v1/admin/**     → marketplace-admin (8091)                   │
└─────────────────────────────────────────────────────────────────────┘
                                │
        ┌───────────────────────┼───────────────────────┐
        │                       │                       │
        ▼                       ▼                       ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│  MySQL 8.0    │    │   Redis 7     │    │   Kafka 7.5   │
│  (Database)   │    │   (Cache)     │    │   (Events)    │
└───────────────┘    └───────────────┘    └───────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     MICROSERVICES                                    │
│                                                                     │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │ marketplace │ │ marketplace │ │ marketplace │ │ marketplace │ │
│  │    auth     │ │   vendor    │ │   product   │ │    cart     │ │
│  │   (8081)    │ │   (8082)    │ │   (8083)    │ │   (8084)    │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘ │
│                                                                     │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │ marketplace │ │ marketplace │ │ marketplace │ │ marketplace │ │
│  │  wishlist   │ │   order     │ │ commission  │ │notification │ │
│  │   (8085)    │ │   (8086)    │ │   (8087)    │ │   (8088)    │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘ │
│                                                                     │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                  │
│  │ marketplace │ │ marketplace │ │ marketplace │                  │
│  │url-shortener│ │   cache     │ │    admin    │                  │
│  │   (8089)    │ │   (8090)    │ │   (8091)    │                  │
│  └─────────────┘ └─────────────┘ └─────────────┘                  │
└─────────────────────────────────────────────────────────────────────┘
```

## Folder Structure

```
marketplace/
├── marketplace-frontend/          # Next.js Frontend
│   ├── src/
│   │   ├── app/                   # Next.js App Router
│   │   │   ├── (auth)/           # Authentication pages
│   │   │   ├── (main)/           # Customer pages
│   │   │   ├── (vendor)/         # Vendor dashboard pages
│   │   │   └── (admin)/          # Admin dashboard pages
│   │   ├── components/            # React components
│   │   ├── hooks/                 # Custom React hooks
│   │   ├── lib/                   # Utility functions
│   │   └── types/                 # TypeScript types
│   ├── Dockerfile
│   └── package.json
│
├── marketplace-gateway/           # Spring Cloud Gateway
│   ├── src/main/java/
│   │   └── com/marketplace/gateway/
│   │       ├── config/           # Configuration classes
│   │       ├── filter/           # Gateway filters
│   │       └── handler/          # Exception handlers
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── Dockerfile
│   └── pom.xml
│
├── shared-library/                # Shared Java Library
│   ├── src/main/java/
│   │   └── com/marketplace/shared/
│   │       ├── dto/common/       # Common DTOs
│   │       ├── exception/        # Exception handling
│   │       ├── security/         # Security utilities
│   │       └── util/             # Utility classes
│   └── pom.xml
│
├── marketplace-auth/              # Authentication Service
├── marketplace-vendor/            # Vendor Management Service
├── marketplace-product/           # Product Management Service
├── marketplace-cart/              # Shopping Cart Service
├── marketplace-wishlist/          # Wishlist Service
├── marketplace-order/             # Order Management Service
├── marketplace-commission/        # Commission & Settlement Service
├── marketplace-notification/      # Notification Service
├── marketplace-url-shortener/     # URL Shortener Service
├── marketplace-cache/             # Cache Service
├── marketplace-admin/             # Admin Dashboard Service
│
├── docker-compose.yml             # Main Docker Compose
├── docker-compose.dev.yml         # Development Docker Compose
├── docker-compose.prod.yml        # Production Docker Compose
├── .env                           # Environment Variables
└── .env.example                   # Environment Variables Template
```

## API Routing

| Path | Service | Port |
|------|---------|------|
| `/api/v1/auth/**` | marketplace-auth | 8081 |
| `/api/v1/vendors/**` | marketplace-vendor | 8082 |
| `/api/v1/products/**` | marketplace-product | 8083 |
| `/api/v1/categories/**` | marketplace-product | 8083 |
| `/api/v1/cart/**` | marketplace-cart | 8084 |
| `/api/v1/wishlist/**` | marketplace-wishlist | 8085 |
| `/api/v1/orders/**` | marketplace-order | 8086 |
| `/api/v1/commissions/**` | marketplace-commission | 8087 |
| `/api/v1/notifications/**` | marketplace-notification | 8088 |
| `/api/v1/urls/**` | marketplace-url-shortener | 8089 |
| `/api/v1/cache/**` | marketplace-cache | 8090 |
| `/api/v1/admin/**` | marketplace-admin | 8091 |
| `/actuator/health` | All services | - |

## Security

### JWT Authentication
- Token-based authentication with access and refresh tokens
- Tokens contain user ID, email, and roles
- Gateway validates tokens and forwards user context to services

### Role-Based Access Control
- **CUSTOMER**: Browse products, manage cart/wishlist, place orders
- **VENDOR**: Manage products, view orders, analytics
- **ADMIN**: Full system access, vendor/product moderation

### Public Endpoints
- `/api/v1/auth/register`
- `/api/v1/auth/login`
- `/api/v1/auth/refresh`
- `/api/v1/products/**` (GET only)
- `/actuator/health`

## Database Strategy

All microservices share a single MySQL database (`multivendor_marketplace`) with Flyway migrations:

| Version | Service | Tables |
|---------|---------|--------|
| V1 | auth | users, roles, user_roles, refresh_tokens |
| V2 | vendor | vendors, vendor_analytics, audit_logs |
| V3 | product | categories, products, product_variants, product_images |
| V4 | cart | carts, cart_items |
| V5 | order | orders, order_items, order_status_history |
| V6 | commission | commission_rules, commission_records, settlements |
| V7 | notification | notifications, notification_templates |
| V8 | wishlist | wishlists, wishlist_items |
| V9 | url-shortener | short_urls, url_clicks |

## Deployment

### Development
```bash
# Start infrastructure only
docker-compose -f docker-compose.dev.yml up -d

# Run services locally
./mvnw spring-boot:run -pl marketplace-auth
```

### Production
```bash
# Build all services
./mvnw clean package -DskipTests

# Start all services
docker-compose -f docker-compose.prod.yml up -d
```

### Health Checks
- Gateway: `http://localhost:8080/actuator/health`
- Auth: `http://localhost:8081/actuator/health`
- Frontend: `http://localhost:3000`
