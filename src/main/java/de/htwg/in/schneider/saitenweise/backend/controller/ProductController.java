package de.htwg.in.schneider.saitenweise.backend.controller;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.htwg.in.schneider.saitenweise.backend.model.Category;
import de.htwg.in.schneider.saitenweise.backend.model.Product;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.model.Role;
import de.htwg.in.schneider.saitenweise.backend.repository.ProductRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    private boolean userFromJwtIsAdmin(Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            LOG.warn("JWT or subject is null");
            return false;
        }
        Optional<User> user = userRepository.findByOauthId(jwt.getSubject());
        if (!user.isPresent() || user.get().getRole() != Role.ADMIN) {
            LOG.warn("Unauthorized access by " + user.map(u -> "user with oauthId " + u.getOauthId())
                    .orElse("unknown user"));
            return false;
        }
        return true;
    }

    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) String name,
            @RequestParam(required = false) Category category) {
        if (name != null && category != null) {
            return productRepository.findByTitleContainingIgnoreCaseAndCategory(name, category);
        } else if (name != null) {
            return productRepository.findByTitleContainingIgnoreCase(name);
        } else if (category != null) {
            return productRepository.findByCategory(category);
        } else {
            return productRepository.findAll();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@AuthenticationPrincipal Jwt jwt, @RequestBody Product product) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }

        if (product.getId() != null) {
            product.setId(null);
            LOG.warn(
                    "Attempted to create a product with an existing ID. ID has been set to null to create a new product.");
        }
        Product newProduct = productRepository.save(product);
        LOG.info("Created new product with id " + newProduct.getId());
        return ResponseEntity.ok(newProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@AuthenticationPrincipal Jwt jwt, 
        @PathVariable Long id, @RequestBody Product productDetails) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }

        Optional<Product> opt = productRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Product product = opt.get();
        product.setCategory(productDetails.getCategory());
        product.setDescription(productDetails.getDescription());
        product.setImageUrl(productDetails.getImageUrl());
        product.setPrice(productDetails.getPrice());
        product.setTitle(productDetails.getTitle());
        Product updatedProduct = productRepository.save(product);
        LOG.info("Updated product with id " + updatedProduct.getId());
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@AuthenticationPrincipal Jwt jwt,@PathVariable Long id) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }

        Optional<Product> opt = productRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(opt.get());
        LOG.info("Deleted product with id " + id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}