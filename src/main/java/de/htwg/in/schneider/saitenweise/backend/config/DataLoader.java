package de.htwg.in.schneider.saitenweise.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.htwg.in.schneider.saitenweise.backend.model.Category;
import de.htwg.in.schneider.saitenweise.backend.model.Product;
import de.htwg.in.schneider.saitenweise.backend.model.Review;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.model.Role;
import de.htwg.in.schneider.saitenweise.backend.repository.ProductRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.ReviewRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Profile("!test")
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, ProductRepository productRepository,
            ReviewRepository reviewRepository) {
        return args -> {
            loadInitialUsers(userRepository);

            // only load products and reviews if none exist
            if (productRepository.count() == 0) { // Check if the repository is empty
                LOGGER.info("Database is empty. Loading initial data...");
                loadInitialData(productRepository, reviewRepository);
            } else {
                LOGGER.info("Database already contains data. Skipping data loading.");
            }
        };
    }

    private void loadInitialUsers(UserRepository userRepository) {
        upsertUser(userRepository, "Johannes Schneider", "jsdump123+1@gmail.com", "auth0|69248a03c95661c67b55fe61",
                Role.REGULAR);
        upsertUser(userRepository, "Admin User", "jsdump123+admin@gmail.com", "auth0|6925d3052f196223d506f863",
                Role.ADMIN);
    }

    private void upsertUser(UserRepository userRepository, String name, String email, String oauthId, Role role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            User e = existing.get();
            e.setName(name);
            e.setOauthId(oauthId);
            e.setRole(role);
            userRepository.save(e);
            LOGGER.info("Updated existing {} user with email={}", role, email);
        } else {
            User u = new User();
            u.setName(name);
            u.setEmail(email);
            u.setOauthId(oauthId);
            u.setRole(role);
            userRepository.save(u);
            LOGGER.info("Created new {} user with email={}", role, email);
        }
    }

    private void loadInitialData(ProductRepository productRepository, ReviewRepository reviewRepository) {
        Product violin = new Product();
        violin.setTitle("Geige Modell Paganini");
        violin.setDescription("Eine hochwertige Geige, welche schon alle Konzerthäuser dieser Welt gesehen hat.");
        violin.setCategory(Category.VIOLIN);
        violin.setPrice(1200.00);
        violin.setImageUrl("https://neshanjo.github.io/saitenweise-images/violin_pro.jpg");

        Product doubleBass = new Product();
        doubleBass.setTitle("Kontrabass Modell Maestro");
        doubleBass
                .setDescription("Ein professioneller Kontrabass, für Klassik- und Jazz geeignet, optimal eingestellt.");
        doubleBass.setCategory(Category.DOUBLE_BASS);
        doubleBass.setPrice(3500.00);
        doubleBass.setImageUrl("https://neshanjo.github.io/saitenweise-images/doublebass_pro.jpg");

        Product strings = new Product();
        strings.setTitle("Geigensaiten Cat Screaming");
        strings.setDescription("Extra dick und robust. Endlich können Sie sich gegen Ihre Katze wehren.");
        strings.setCategory(Category.ACCESSORIES);
        strings.setPrice(30.00);
        strings.setImageUrl("https://neshanjo.github.io/saitenweise-images/accessory_violin_strings.jpg");

        Product celloBlack = new Product();
        celloBlack.setTitle("Cello Carbon-Optik Black");
        celloBlack.setDescription("Ein modernes Cello in edlem Schwarz. Robuste Bauweise mit klarem, kräftigem Klang.");
        celloBlack.setCategory(Category.CELLO);
        celloBlack.setPrice(1850.00);
        celloBlack.setImageUrl("https://neshanjo.github.io/saitenweise-images/cello_black.jpg");

        Product celloCracked = new Product();
        celloCracked.setTitle("Cello 'Vintage' mit Charakter");
        celloCracked.setDescription(
                "Dieses Instrument hat Geschichte und einige Risse, die aber professionell repariert wurden. " + 
                "Ein Bogen ist mit dabei. Diese muss aber neu bespannt werden.");
        celloCracked.setCategory(Category.CELLO);
        celloCracked.setPrice(1450.00);
        celloCracked.setImageUrl("https://neshanjo.github.io/saitenweise-images/cello_cracked.jpg");

        Product doubleBassFlames = new Product();
        doubleBassFlames.setTitle("Kontrabass Rockabilly Fire");
        doubleBassFlames.setDescription(
                "Heißer Sound für Slap-Bass-Enthusiasten. Mit handgemaltem Flammen-Design ein echter Hingucker.");
        doubleBassFlames.setCategory(Category.DOUBLE_BASS);
        doubleBassFlames.setPrice(2900.00);
        doubleBassFlames.setImageUrl("https://neshanjo.github.io/saitenweise-images/doublebass_flames.jpg");

        // Viola Beginner
        Product violaBeginner = new Product();
        violaBeginner.setTitle("Bratsche Schülermodell");
        violaBeginner.setDescription("Die perfekte Einstiegs-Viola mit leichter Ansprache und inklusivem Kinnhalter.");
        violaBeginner.setCategory(Category.VIOLA);
        violaBeginner.setPrice(650.00);
        violaBeginner.setImageUrl("https://neshanjo.github.io/saitenweise-images/viola_beginner.jpg");

        // Viola Profi
        Product violaPro = new Product();
        violaPro.setTitle("Bratsche Solistenmodell");
        violaPro.setDescription(
                "Handgefertigte Viola aus ausgesuchten Tonhölzern für einen dunklen, warmen und tragenden Ton.");
        violaPro.setCategory(Category.VIOLA);
        violaPro.setPrice(2400.00);
        violaPro.setImageUrl("https://neshanjo.github.io/saitenweise-images/viola_pro.jpg");

        // Geige Beginner
        Product violinBeginner = new Product();
        violinBeginner.setTitle("Geigenset für Anfänger");
        violinBeginner.setDescription(
                "Komplettes Set inklusive Bogen und Koffer. Alles, was man für die erste Unterrichtsstunde braucht.");
        violinBeginner.setCategory(Category.VIOLIN);
        violinBeginner.setPrice(399.00);
        violinBeginner.setImageUrl("https://neshanjo.github.io/saitenweise-images/violin_beginner.jpg");

        // Cello Saiten (Zubehör)
        Product celloStrings = new Product();
        celloStrings.setTitle("Premium Cello-Saitensatz");
        celloStrings.setDescription("Präzisionsgesponnene Stahlsaiten für höchste Brillanz und Langlebigkeit.");
        celloStrings.setCategory(Category.ACCESSORIES);
        celloStrings.setPrice(125.00);
        celloStrings.setImageUrl("https://neshanjo.github.io/saitenweise-images/accessory_cello_strings.jpg");

        // Kolophonium (Zubehör)
        Product rosin = new Product();
        rosin.setTitle("Kontrabass-Kolophonium Schrummel");
        rosin.setDescription(
                "Extra haftstarkes Kolophonium, speziell für die dicken Saiten des Kontrabasses entwickelt.");
        rosin.setCategory(Category.ACCESSORIES);
        rosin.setPrice(15.50);
        rosin.setImageUrl("https://neshanjo.github.io/saitenweise-images/accessory_doublebass_rosin.jpg");

        productRepository.saveAll(Arrays.asList(
                violin,
                doubleBass,
                strings,
                celloBlack,
                celloCracked,
                doubleBassFlames,
                violaBeginner,
                violaPro,
                violinBeginner,
                celloStrings,
                rosin));

        // Add reviews
        Review r1a = new Review();
        r1a.setStars(5);
        r1a.setText("Fantastisches Instrument, klingt wunderbar!");
        r1a.setUserName("Anna");
        r1a.setProduct(violin);
        Review r1b = new Review();
        r1b.setStars(4);
        r1b.setText("Bin ziemlich zufrieden.");
        r1b.setUserName("Oli");
        r1b.setProduct(violin);

        Review r2 = new Review();
        r2.setStars(4);
        r2.setText("Sehr guter Bass, aber schwer zu transportieren.");
        r2.setUserName("Ben");
        r2.setProduct(doubleBass);

        Review r3 = new Review();
        r3.setStars(3);
        r3.setText("Saiten sind ok, aber nicht besonders langlebig.");
        r3.setUserName("Chris");
        r3.setProduct(strings);

        reviewRepository.saveAll(Arrays.asList(r1a, r1b, r2, r3));
        LOGGER.info("Initial data loaded successfully.");
    }
}
