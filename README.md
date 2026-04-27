# Saitenweise Backend

Saitenweise is a webshop for string instruments. It sells violins, violas, cellos, double basses, and accessories (strings, bows, rosin). This is the Backend for the shop.

## How to Run

To run the application, ensure you have Maven installed. Then execute the following command in the project root directory:

```sh
mvn spring-boot:run
```

The application will start on `http://localhost:8081`.

## Iterations

### Iteration 0: Added Dockerfile

- this is not needed yet but helpfu

### Iteration 1a: First REST Controller

- basic project configuration in `application.properties`
- a first simple REST-Controller `ProductController` for GET request to `/api/product` that returns a list of strings.
- test e.g. with using `curl`: `curl http://localhost:8081/api/product`
