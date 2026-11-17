\# E-Commerce Microservices



An e-commerce backend built as a set of independent Spring Boot microservices. It was migrated step by step from a monolith, with the focus on clean service boundaries, event-driven communication with Kafka, and consistent code structure across services.



\*\*Tech stack:\*\* Java 25, Spring Boot 4.1, Spring Security (JWT), Spring Data JPA, PostgreSQL 18, Redis 8, Apache Kafka 4 (KRaft), Gradle (Kotlin DSL), Docker Compose.



\## Architecture



Each service is an independent Gradle project with its own database and no shared code. Services talk to each other in two ways:



\- \*\*Synchronous (REST):\*\* Spring HTTP Interface (`@HttpExchange`) on top of `RestClient`. The user's JWT is relayed to downstream services.

\- \*\*Asynchronous (Kafka):\*\* domain events published through a port/adapter pair and consumed by idempotent listeners.



```mermaid

flowchart LR

&#x20;   order -- REST --> cart

&#x20;   order -- REST --> product

&#x20;   order -- REST --> user

&#x20;   cart -- REST --> product

&#x20;   payment -- REST --> order

&#x20;   review -- REST --> product

&#x20;   review -- REST --> user

&#x20;   review -- REST --> order



&#x20;   auth -- "user-registered, \*-requested" --> Kafka{{Kafka}}

&#x20;   Kafka -- "user-registered" --> user

&#x20;   Kafka -- "verification / login / password-reset requested" --> notification

&#x20;   order -- "order-placed, order-cancelled" --> Kafka

&#x20;   Kafka -- "order-placed" --> cart

&#x20;   Kafka -- "order-cancelled" --> product

&#x20;   payment -- "payment-confirmed, payment-refunded" --> Kafka

&#x20;   Kafka -- "payment-\*" --> order

```



\### Services



| Service | Port | Database | Responsibility |

|---|---|---|---|

| `auth-service` | 8081 | PostgreSQL 5432 | Registration, email verification, two-step login (email OTP), JWT access/refresh cookies, password reset, rate limiting |

| `user-service` | 8082 | PostgreSQL 5433 | User profiles and addresses |

| `product-service` | 8083 | PostgreSQL 5434 | Categories, products, images, stock |

| `cart-service` | 8084 | PostgreSQL 5435, Redis 6379 | Shopping cart (product data cached in Redis) |

| `order-service` | 8085 | PostgreSQL 5436 | Order placement, cancellation, status |

| `payment-service` | 8086 | PostgreSQL 5437 | Payments (strategy pattern; cash on delivery implemented), confirm and refund |

| `review-service` | 8087 | PostgreSQL 5438 | Product reviews and ratings (only for delivered purchases) |

| `notification-service` | none | none | Sends emails (SMTP) from Kafka events |



Infrastructure (`infrastructure/`): Kafka (`localhost:9092`), Kafbat UI (`http://localhost:8090`), Mailpit (SMTP `1025`, UI `http://localhost:8025`).



\### Kafka topics



| Topic | Producer | Consumer |

|---|---|---|

| `user-registered` | auth | user |

| `verification-code-requested`, `login-code-requested`, `password-reset-requested` | auth | notification |

| `order-placed` | order | cart (removes purchased items from the cart) |

| `order-cancelled` | order | product (restores stock, deduplicated by `eventId`) |

| `payment-confirmed`, `payment-refunded` | payment | order (updates order status) |



Consumers use retry with a dead-letter topic and `ErrorHandlingDeserializer`, so a malformed message does not block a partition.



\### Design decisions



\- \*\*Database per service\*\*, no shared tables, no shared library. Small pieces (`ErrorCode`, `BaseException`, `ApiResponse`, Kafka consumer config) are intentionally copied into each service to keep them independently deployable.

\- \*\*Feature-based packages\*\* (`order/`, `cart/`, ...). Cross-service code lives under `messaging/sync` (REST clients) and `messaging/async` (Kafka producers and consumers).

\- \*\*Port/adapter for outgoing messages\*\*, so business code does not depend on Kafka.

\- \*\*Unified error handling:\*\* an `ErrorCode` enum, one exception class per case extending `BaseException`, and a single `@RestControllerAdvice` that maps them to HTTP status codes.

\- \*\*Idempotent consumers\*\* where an event has side effects (stock restore uses a `processed\_events` table).

\- \*\*Stateless JWT\*\* validation in every service with a shared signing key. Roles: `ROLE\_USER`, `ROLE\_STAFF`, `ROLE\_ADMIN`.

\- \*\*Monorepo:\*\* all services live in one repository for easier review; CI is intended to build only the changed service.



\## Running locally



\### Prerequisites



\- JDK 25

\- Docker with Docker Compose



\### 1. Start infrastructure



Every service has its own `compose.yaml` for its database, and `infrastructure/` holds Kafka and Mailpit. Run `docker compose up -d` in each folder you need:



```bash

cd infrastructure/kafka   \&\& docker compose up -d \&\& cd ../..

cd infrastructure/mailpit \&\& docker compose up -d \&\& cd ../..

cd auth-service           \&\& docker compose up -d \&\& cd ..

\# repeat for user-service, product-service, cart-service, order-service,

\# payment-service and review-service

```



\### 2. Create local configuration



`application-local.yaml` files are git-ignored. Copy the provided examples in each service:



```powershell

Get-ChildItem -Recurse -Filter application-local.yaml.example |

&#x20; ForEach-Object { Copy-Item $\_.FullName ($\_.FullName -replace '\\.example$','') }

```



> The example values (database passwords, JWT secret, admin account) are for \*\*local development only\*\*. Never reuse them in a real environment.



\### 3. Create Kafka topics (optional)



Topics are auto-created with a single partition on first use, which is enough for local work. To create one explicitly with three partitions:



```bash

docker exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 \\

&#x20; --create --if-not-exists --topic order-placed --partitions 3 --replication-factor 1

```



\### 4. Start the services



In each service folder:



```bash

./gradlew bootRun        # Windows: .\\gradlew bootRun

```



The `local` profile is active by default. Start at least `auth-service` and `notification-service` to be able to log in; add the others as you need them.



\### 5. Try it



1\. `POST /api/v1/auth/signup` registers a user. The verification code is emailed through `notification-service`; open Mailpit at `http://localhost:8025` to read it.

2\. `POST /api/v1/auth/verify?email=...\&code=...` activates the account.

3\. Login has two steps: `POST /api/v1/auth/login` sends a code, then `POST /api/v1/auth/login/verify?email=...\&code=...` returns the JWT cookies.

4\. A development admin account is seeded on first start (see `auth-service/src/main/resources/application-local.yaml.example`).



\## Project status



Done:



\- 8 services with consistent structure and error handling

\- Kafka event flows with retry and dead-letter topics

\- Redis cache for cart product lookups

\- Manual API testing with Postman



Planned:



\- \[ ] Flyway database migrations (currently `ddl-auto: update`)

\- \[ ] Automated tests (JUnit, Mockito, Testcontainers including Kafka)

\- \[ ] GitHub Actions CI

\- \[ ] Dockerfiles for all services

\- \[ ] Kubernetes (Helm, Envoy Gateway, secrets) and CD

\- \[ ] Observability (Loki, Grafana)

\- \[ ] Redis-backed distributed rate limiting

\- \[ ] Transactional outbox for reliable event publishing



\## Known limitations



\- Events are published after the database commit, without a transactional outbox, so an event can be lost if publishing fails (planned fix above).

\- Verification and login codes travel through Kafka topics in plain text (acceptable for local development; needs short retention or a different design for production).

\- Internal endpoints (`/api/v1/internal/\*\*`) are not yet blocked at a gateway; this is planned for the Kubernetes stage.

\- Requests without a token currently return 403 instead of 401.

