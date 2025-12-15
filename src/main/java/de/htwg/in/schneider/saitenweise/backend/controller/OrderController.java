package de.htwg.in.schneider.saitenweise.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import de.htwg.in.schneider.saitenweise.backend.dto.OrderItem;
import de.htwg.in.schneider.saitenweise.backend.dto.OrderRequest;
import de.htwg.in.schneider.saitenweise.backend.model.Product;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.repository.ProductRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Value("${spring.mail.username}")
    private String mailSenderUsername;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JavaMailSender emailSender;

    @PostMapping
    public ResponseEntity<String> placeOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody OrderRequest orderRequest) {
        if (jwt == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String oauthId = jwt.getSubject();
        Optional<User> userOpt = userRepository.findByOauthId(oauthId);

        if (!userOpt.isPresent()) {
            LOG.warn("Order attempted by unknown user: {}", oauthId);
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("User has no email address");
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("Vielen Dank für Ihre Bestellung, ").append(user.getName()).append("!\n\n");
        receipt.append("Lieferadresse:\n");
        if (orderRequest.getAddress() != null) {
            receipt.append(orderRequest.getAddress().toString());
        } else {
            receipt.append("Keine Adresse angegeben.");
        }
        receipt.append("\n\nBestelldetails:\n");
        receipt.append("------------------------------------------------\n");

        double total = 0.0;
        boolean allProductsFound = true;

        for (OrderItem item : orderRequest.getItems()) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product p = productOpt.get();
                double lineTotal = p.getPrice() * item.getQuantity();
                total += lineTotal;
                receipt.append(String.format("%-30s x%d   %8.2f €\n", p.getTitle(), item.getQuantity(), lineTotal));
            } else {
                allProductsFound = false;
                receipt.append(String.format("Produkt ID %d nicht gefunden x%d\n", item.getProductId(), item.getQuantity()));
            }
        }

        receipt.append("------------------------------------------------\n");
        receipt.append(String.format("Gesamt:                           %8.2f €\n", total));

        if (!allProductsFound) {
            return ResponseEntity.badRequest().body("One or more products in the order could not be found.");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Saitenweise <" + mailSenderUsername + ">");
            message.setTo(user.getEmail());
            message.setSubject("Ihre Saitenweise Bestellbestätigung");
            message.setText(receipt.toString());
            emailSender.send(message);
            LOG.info("Order receipt sent to {}", user.getEmail());
        } catch (Exception e) {
            LOG.error("Failed to send email", e);
            return ResponseEntity.internalServerError().body("Failed to send email receipt.");
        }

        return ResponseEntity.ok("Order placed and receipt sent.");
    }
}
