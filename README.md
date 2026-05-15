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

## Iteration 5: Added 1:n relation Product - Review

- Added `Review` entity with bidirectional relation to `Product`, see `Product#reviews` and `Review#product` with the corresponding Annotations.
- **Important**: In order to avoid endless recursion when serializing these types to JSON in the rest controller, `Product#reviews` has `@JsonIgnore`. Hence, all REST endpoints returning products do not include the reviews of a product.
  - To get the reviews of a product, you have to call `/api/review/product/<id of product>`.
- Also note the `@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })` on every Entity which avoids problems with serialization JPA entities to JSON.
- Added Create, Read and Delete for Reviews in `ReviewController`.
  - To create a review for the product with id 1, POST this content to `/api/review`:

      ```json
      {
         "userName": "Meckerer",
         "text": "ziemlich schlecht",
         "stars": 1,
         "product": {
            "id": 1
         }
      }
      ```

  - this and other example requests can be found in `src/test/bruno` which can be opened with the [Bruno API Client](https://www.usebruno.com/).
- In `DataLoader`, some example reviews are added to the database.

## Iteration 6: Search and filter products

- Added request parameters `name` and `category` in `ProductController#getProducts` to search by name and filter by category
- Added corresponding repository methods
- Added endpoint to list all categories (and their German translations) to make them available for selection in the client, see `CategoryController`.
- added more initial Data in `DataLoader`

## Iteration 7: Added user profile and spring security (OAuth2 with Auth0)

- Added auth0 dependency to `pom.xml`
- Configuration of auth0 in `applications.properties`
  - Use your own issuer and audience values when copying
  - also available: logging config for more verbose spring security logging to debug errors
- Enabling OAuth2 / Spring Security in `SecurityConfig.java`.
- Added `User` entity and repository
- Loading initial users in `DataLoader`.
  - When copying this, create users in Auth0 first and insert their OAuthID to the DataLoader
- New endpoint `/api/profile` via `ProfileController`.
  - called with a valid bearer token, it loads the user data from the backend
