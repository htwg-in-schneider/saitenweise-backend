# Saitenweise Backend

Saitenweise is a webshop for string instruments. It sells violins, violas, cellos, double basses, and accessories (strings, bows, rosin). This is the Backend for the shop.

## How to Run

To run the application, ensure you have Maven installed. Then execute the following command in the project root directory:

```sh
mvn spring-boot:run
```

The application will start on `http://localhost:8081`.

## Iterations

### Iteration 1a: First REST Controller

- basic project configuration in `application.properties`
- a first simple REST-Controller `ProductController` for GET request to `/api/product` that returns a list of strings.
- test e.g. with using `curl`: `curl http://localhost:8081/api/product` or bruno (see bruno project in `src/test/bruno`)

### Iteration 1b: JSON (de)serialization

- REST-Controller `ProductController` supporting GET and POST requests to `/api/product`
- using Java Objects instead of strings
- test GET with using `curl`: `curl http://localhost:8081/api/product`
- test POST with `curl -X POST http://localhost:8081/api/product -H 'Content-Type: application/json' -d '{"title":"Bratsche","description":"macht bratsch"}'` or in bruno

### Iteration 1c: REST-Controller with model class

- introduced model classes `Product` and `Category`
- `ProductController` returns some example data that can be consumed by the frontend

### Iteration 2: CORS Configuration

In this iteration, a `WebConfig` for a global CORS (Cross-Origin Resource Sharing) configuration was added to the backend. This allows the frontend application, which may be served from a different origin, to access the backend APIs without running into cross-origin issues.

### Iteration 3: Database Integration

In this iteration, the application was updated to integrate with a database. The following changes were made:

1. **Database Configuration**:
   - Added support for H2 and MariaDB (`pom.xml`)
   - Updated the `application.properties` to include database configurations
2. **Product Entity**:
   - The `Product` class was annotated with JPA annotations to map it to a database table.
   - Added missing `equals` and `hashCode` methods.
3. **Product Repository**:
   - A new `ProductRepository` interface was created to handle database operations for the `Product` entity.
4. **Data Loader**:
   - `config.DataLoader` is a CommandLineRunner that is run during application startup and used to fill initial data into the database. It is only run when no products are defined yet.
5. **Product Controller**:

### Iteration 4: CRUD for Products

In this iteration the backend was extended to support full CRUD operations for products.

- CRUD Endpoints (REST)
  - GET /api/product — list all products
  - GET /api/product/{id} — fetch a single product (404 if not found)
  - POST /api/product — create a product (returns 201)
  - PUT /api/product/{id} — update an existing product (404 if not found)
  - DELETE /api/product/{id} — delete a product (204 on success, 404 if not found)
- No Validation: Entities are not validated before written to the database!
- Example request to be used in bruno were added to `src/test/bruno`
