package de.htwg.in.schneider.saitenweise.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.htwg.in.schneider.saitenweise.backend.model.Category;
import de.htwg.in.schneider.saitenweise.backend.model.Product;
import de.htwg.in.schneider.saitenweise.backend.repository.ProductRepository;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    public CommandLineRunner loadData(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) { // Check if the repository is empty
                LOGGER.info("Database is empty. Loading initial data...");
                loadInitialData(repository);
            } else {
                LOGGER.info("Database already contains data. Skipping data loading.");
            }
        };
    }

    private void loadInitialData(ProductRepository repository) {
        Product violin = new Product();
        violin.setTitle("Geige Modell Paganini");
        violin.setDescription("Eine hochwertige Geige, welche schon alle Konzerthäuser dieser Welt gesehen hat.");
        violin.setCategory(Category.VIOLIN);
        violin.setPrice(1200.00);
        violin.setImageUrl("https://neshanjo.github.io/saitenweise-images/violin_pro.jpg");

        Product doubleBass = new Product();
        doubleBass.setTitle("Kontrabass Modell Maestro");
        doubleBass.setDescription("Ein professioneller Kontrabass, für Klassik- und Jazz geeignet, optimal eingestellt.");
        doubleBass.setCategory(Category.DOUBLE_BASS);
        doubleBass.setPrice(3500.00);
        doubleBass.setImageUrl("https://neshanjo.github.io/saitenweise-images/doublebass_pro.jpg");

        Product strings = new Product();
        strings.setTitle("Geigensaiten Cat Screaming");
        strings.setDescription("Extra dick und robust. Endlich können Sie sich gegen Ihre Katze wehren.");
        strings.setCategory(Category.ACCESSORIES);
        strings.setPrice(30.00);
        strings.setImageUrl("https://neshanjo.github.io/saitenweise-images/accessory_violin_strings.jpg");

        repository.saveAll(Arrays.asList(violin, doubleBass, strings));
        LOGGER.info("Initial data loaded successfully.");
    }
}