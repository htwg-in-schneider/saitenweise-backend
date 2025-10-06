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

### Iteration 3: Database Integration

In this iteration, the application was updated to integrate with a database. The following changes were made:

1. **Database Configuration**:
   - Added support for H2 (local profile) and MariaDB (dev and production profile), introducing a new dev profile to develop with a local MariaDB.
   - Updated the `application.properties`, `application-dev.properties` and `application-prod.properties.example` files to include database configurations - the latter must be copied to `application-prod.properties` first (which then is excluded from git via `.gitignore`).
   - To install and start MariaDB on a Mac, use

      ```sh
      brew install mariadb
      brew services start mariadb
      ```

   - To setup a user and a new database, first open mysql console with `sudo mysql`, then run the following commands:

      ```sql
      CREATE DATABASE saitenweise;
      CREATE USER 'user'@'localhost' IDENTIFIED BY 'deinpasswort';
      GRANT ALL PRIVILEGES ON saitenweise.* TO 'user'@'localhost';
      FLUSH PRIVILEGES;
      ```

2. **Dependencies**:
   - Added dependencies for H2 and MariaDB in the `pom.xml` file.

3. **Product Entity**:
   - The `Product` class was annotated with JPA annotations to map it to a database table.
   - Added missing `equals` and `hashCode` methods.
   - Added a `toString` method for better debugging.

4. **Product Repository**:
   - A new `ProductRepository` interface was created to handle database operations for the `Product` entity.

5. **Data Loader**:
   - `config.DataLoader` is a CommandLineRunner that is run during application startup and used to fill initial data into the database. It is only run when no products are defined yet.

6. **Product Controller**:
   - Updated the `/api/product` endpoint to fetch products from the database instead of returning hardcoded values.

7. **Testing**:
   - Enabled the `test` profile for tests by adding the `@ActiveProfiles("test")` annotation in the test class.

To run the application with database integration, ensure the database is set up and the appropriate profile is activated. For example, to use the local profile:

```sh
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Iteration 4: CORS Configuration

In this iteration, a `WebConfig` for a global CORS (Cross-Origin Resource Sharing) configuration was added to the backend. This allows the frontend application, which may be served from a different origin, to access the backend APIs without running into cross-origin issues.

### Iteration 5: Added GitHub Actions workflow for Maven build verification

- compiles project and runs tests
- see `.github/workflows/verify.yml`