package de.htwg.in.schneider.saitenweise.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.htwg.in.schneider.saitenweise.backend.model.Category;
import de.htwg.in.schneider.saitenweise.backend.model.Product;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @GetMapping
    public List<Product> getProducts() {
        Product violin = new Product();
        violin.setId(1);
        violin.setTitle("Geige Modell Paganini");
        violin.setDescription("Eine hochwertige Geige, welche schon alle Konzerthäuser dieser Welt gesehen hat.");
        violin.setCategory(Category.VIOLIN);
        violin.setPrice(1200.00);
        violin.setImageUrl("https://neshanjo.github.io/saitenweise-images/violin_pro.jpg");

        Product doubleBass = new Product();
        doubleBass.setId(2);
        doubleBass.setTitle("Kontrabass Modell Maestro");
        doubleBass.setDescription("Ein professioneller Kontrabass, für Klassik- und Jazz geeignet, optimal eingestellt.");
        doubleBass.setCategory(Category.DOUBLE_BASS);
        doubleBass.setPrice(3500.00);
        doubleBass.setImageUrl("https://neshanjo.github.io/saitenweise-images/doublebass_pro.jpg");

        Product strings = new Product();
        strings.setId(3);
        strings.setTitle("Geigensaiten Cat Screaming");
        strings.setDescription("Extra dick und robust. Endlich können Sie sich gegen Ihre Katze wehren.");
        strings.setCategory(Category.ACCESSORIES);
        strings.setPrice(30.00);
        strings.setImageUrl("https://neshanjo.github.io/saitenweise-images/accessory_violin_strings.jpg");

        return Arrays.asList(violin, doubleBass, strings);
    }
}