package de.htwg.in.schneider.saitenweise.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @GetMapping
    public List<String> getProducts() {
        return Arrays.asList("Geige Modell Paganini",
                "Kontrabass Modell Maestro",
                "Geigensaiten Cat Screaming");
    }
}