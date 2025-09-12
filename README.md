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
