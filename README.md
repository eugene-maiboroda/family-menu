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

The codebase is organized as a feature-first modular monolith. It is one Spring Boot application and one Maven project, while business capabilities are separated by top-level Java packages:

```text
com.familymenu.backend
├── product/
├── dish/
├── menu/
├── shopping/
├── infrastructure/
└── bootstrap/
```

Each business module owns its use cases, domain model, persistence model, and HTTP boundary where applicable. Internal structure is added only when the module is complex enough to benefit from it:

- `product` owns the canonical ingredient catalog and the `products` table.
- `dish` owns dishes, recipes, dish ingredients, HTTP endpoints, and the `dishes`/`dish_ingredients` tables.
- `menu` owns menu composition and the `menus`/`menu_dishes` tables.
- `shopping` owns shopping-list orchestration, unit conversion, and aggregation. Shopping lists persist their menu, dish and manual-product sources and calculate totals on read.
- `infrastructure` contains only application-wide technical configuration and bean conventions.
- `bootstrap` contains startup behavior and typed configuration properties.

Modules communicate through small in-process Java contracts:

```text
dish     → product.api
menu     → dish.api
shopping → menu.api
shopping → dish.api
```

A module must not access another module's domain, service, repository, entity, mapper, or web classes. Module API DTO, HTTP DTO, domain models, and JPA entities are separate representations. Local interfaces or ports are introduced only when replaceability or isolation provides a concrete benefit.

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
- extend shopping-list behavior as product requirements grow;
- add image upload through a replaceable storage adapter;
- introduce Flyway database migrations;
- add unit and integration test coverage;
- document the API with OpenAPI;
- add continuous integration checks.

## Current mobile MVP API

The canonical catalog is now `Product` (`/products`) and supports food and household goods. Dish ingredients reference products. Saved shopping lists support an automatic Inbox, named lists, selected menus, dishes and manual purchases. GET returns calculated totals. See [saved shopping lists](docs/saved-shopping-lists-api.md). See [the API and migration guide](docs/mobile-mvp-api.md) for payloads, existing-database upgrades, test setup, and phone connectivity.
