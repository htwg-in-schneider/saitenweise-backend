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
- test e.g. with using `curl`: `curl http://localhost:8081/api/product`

### Iteration 1b: JSON (de)serialization

- REST-Controller `ProductController` supporting GET and POST requests to `/api/product`
- using Java Objects instead of strings
- test GET with using `curl`: `curl http://localhost:8081/api/product`
- test POST with `curl -X POST http://localhost:8081/api/product -H 'Content-Type: application/json' -d '{"name":"Bratsche","description":"macht bratsch"}'`

### Iteration 1c: REST-Controller with model class

- introduced model classes `Product` and `Category`
- `ProductController` returns some example data that can be consumed by the frontend

### Iteration 2: CORS Configuration

In this iteration, a `WebConfig` for a global CORS (Cross-Origin Resource Sharing) configuration was added to the backend. This allows the frontend application, which may be served from a different origin, to access the backend APIs without running into cross-origin issues.
