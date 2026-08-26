# Family Menu Backend

Backend service for a family meal-planning application. The project is designed to keep reusable dishes and recipes in one place, build menus from saved dishes, and generate an aggregated shopping list from their ingredients.

> **Development status:** This project is actively under development. The domain model and core application/persistence structure are being implemented, while parts of the HTTP API, validation, storage integration, database migrations, and automated test coverage are still in progress.

## Product idea

A user creates a personal collection of dishes with ingredients, quantities, measurement units, and optional preparation steps. Saved dishes can then be combined into a menu. The application calculates a shopping list by grouping matching ingredients and aggregating compatible quantities.

The intended first complete workflow is:

1. Create and manage dishes with ingredients.
2. Optionally add ordered recipe steps.
3. Build a menu from saved dishes.
4. Generate an aggregated shopping list.
5. Update or remove previously created data.

## Architecture

The codebase follows a simplified Hexagonal Architecture:

```text
Web adapters
    ↓
Input ports
    ↓
Application services
    ↓
Domain models
    ↓
Output ports
    ↓
Persistence and storage adapters
```

The main packages are organized by responsibility:

- `domain` contains business models and shopping-list calculation rules.
- `application` coordinates dish, menu, and shopping-list use cases.
- `port.in` defines capabilities exposed by the application layer.
- `port.out` defines persistence and infrastructure dependencies.
- `adapter.persistence` contains JPA repositories, entities, mappers, and persistence services.
- `infrastructure` contains shared configuration and exception handling.
- `bootstrap` contains startup configuration.

Persistence entities remain inside the persistence adapter. Domain and application code work with domain models through ports rather than depending directly on JPA.

## Data model

The planned PostgreSQL model includes:

- dishes and their ordered recipe steps;
- a canonical ingredient catalog;
- dish ingredients with quantities and units;
- menus;
- relational links between menus and dishes.

Recipe steps are represented as ordered value objects and are intended to be stored as JSONB because they belong exclusively to a dish. Ingredients and menu relationships remain relational to preserve identity and referential integrity.

## Technology stack

- Java 21
- Spring Boot and Spring Web MVC
- Spring Data JPA and Hibernate
- PostgreSQL and JSONB
- Jakarta Validation
- MapStruct and Lombok
- OpenAPI/Swagger
- Maven
- Docker and Docker Compose

## Local configuration

Create a local environment file from the committed template:

```shell
cp docker/.env.example docker/.env
```

Replace the placeholder database credentials in `docker/.env`. The local file is ignored by Git.

Environment variables:

| Variable | Purpose | Example |
| --- | --- | --- |
| `API_PORT` | Published backend port | `8082` |
| `POSTGRES_PORT` | PostgreSQL port exposed on localhost | `5436` |
| `POSTGRES_DB` | PostgreSQL database name | `familymenu_db` |
| `POSTGRES_USER` | PostgreSQL user | `change_me` |
| `POSTGRES_PASSWORD` | PostgreSQL password | `change_me` |

## Run with Docker

From the `docker` directory:

```shell
docker compose up --build
```

The backend uses the `prod` Spring profile and connects to PostgreSQL through the internal Docker network. The database port is bound to `127.0.0.1` and is not exposed on external network interfaces.

## Run locally

Start PostgreSQL through Docker, export the variables from `docker/.env`, and run the application with the `dev` profile:

```shell
cd docker
docker compose up -d postgres-familymenu-backend
set -a
source .env
set +a
cd ..
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

When the web layer is available, API documentation will be exposed through the configured Springdoc OpenAPI integration.

## Roadmap

- complete dish and menu use cases;
- expose and validate the HTTP API;
- finalize shopping-list aggregation and compatible unit conversion;
- add image upload through a replaceable storage adapter;
- introduce Flyway database migrations;
- add unit and integration test coverage;
- document the API with OpenAPI;
- add continuous integration checks.
