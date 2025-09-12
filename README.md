# Saitenweise Backend

Saitenweise is a webshop for string instruments. It sells violins, violas, cellos, double basses, and accessories (strings, bows, rosin). This is the Backend for the shop.

## How to Run

To run the application, ensure you have Maven installed. Then execute the following command in the project root directory:

```sh
mvn spring-boot:run
```

The application will start on `http://localhost:8081`.

## Iteration

### Iteration 1: First REST Controller

In this iteration, we implemented a REST controller for the `/api/product` endpoint. It returns a hardcoded list of three products: a violin, a double bass, and violin strings. The product details are provided in German.

You can test the `/api/product` endpoint using `curl`:

```sh
curl http://localhost:8081/api/product
```

### Iteration 2: Spring Profiles Configuration

This project now uses Spring profiles for environment-specific configurations. The following profiles are available:

1. **Local Profile**:
   - Configuration file: `src/main/resources/application-local.properties`
   - Uses an H2 database with a file stored in the `target` directory.

2. **Prod Profile**:
   - Configuration file: `src/main/resources/application-prod.properties`
   - Uses a MySQL database with dummy configuration options.

#### Activating Profiles

To activate a profile, set the `spring.profiles.active` property:

- Command-line argument: `-Dspring.profiles.active=local`
- Environment variable: `SPRING_PROFILES_ACTIVE=prod`

To start the application with the `local` profile using Maven, run:

```sh
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Replace `local` with `prod` to use the production profile.
