package de.htwg.in.schneider.saitenweise.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    public static class Product {
        private String name;
        private String description;

        public Product(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    @GetMapping
    public List<Product> getProducts() {
        return Arrays.asList(new Product("Geige Modell Paganini", "eine hochwertige Geige"),
                new Product("Kontrabass Modell Maestro", "ein toller Kontrabass"),
                new Product("Geigensaiten Cat Screaming", "Anfänger-Geigensaiten"));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        System.out.println("Controller called for product: " + product.getName() + " - " + product.getDescription());
        return ResponseEntity.ok("POST successful");
    }
}