package de.htwg.in.schneider.saitenweise.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import de.htwg.in.schneider.saitenweise.backend.model.Review;
import de.htwg.in.schneider.saitenweise.backend.model.Product;
import de.htwg.in.schneider.saitenweise.backend.repository.ReviewRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Review> getAllReviews() {
        LOG.info("Fetching all reviews");
        return reviewRepository.findAll();
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProduct(@PathVariable Long productId) {
        LOG.info("Fetching reviews for product id {}", productId);
        return reviewRepository.findByProductId(productId);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        LOG.info("Attempting to create review for product id {}", 
            review.getProduct() != null ? review.getProduct().getId() : null);
        if (review.getStars() < 1 || review.getStars() > 5) {
            LOG.warn("Review stars out of bounds: {}", review.getStars());
            return ResponseEntity.badRequest().build();
        }
        if (review.getProduct() == null || review.getProduct().getId() == null) {
            LOG.warn("Review product is null or has no id");
            return ResponseEntity.badRequest().build();
        }
        Optional<Product> productOpt = productRepository.findById(review.getProduct().getId());
        if (productOpt.isEmpty()) {
            LOG.warn("Product not found for review: {}", review.getProduct().getId());
            return ResponseEntity.badRequest().build();
        }
        review.setProduct(productOpt.get());
        Review saved = reviewRepository.save(review);
        LOG.info("Created review with id {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable Long id) {
        LOG.info("Attempting to delete review with id {}", id);
        Optional<Review> opt = reviewRepository.findById(id);
        if (opt.isPresent()) {
            Review review = opt.get();
            reviewRepository.delete(review);
            LOG.info("Deleted review with id {}", id);
            return ResponseEntity.noContent().build();
        } else {
            LOG.warn("Review not found for deletion: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
