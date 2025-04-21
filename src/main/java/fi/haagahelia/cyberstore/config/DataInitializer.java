package fi.haagahelia.cyberstore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fi.haagahelia.cyberstore.domain.Product;
import fi.haagahelia.cyberstore.domain.ProductRepository;
import fi.haagahelia.cyberstore.domain.User;
import fi.haagahelia.cyberstore.domain.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Create admin user if it doesn't exist
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.setRoles(new HashSet<>(Collections.singletonList("ADMIN")));
                admin.setEnabled(true);
                userRepository.save(admin);
                System.out.println("Admin user created");
            }

            // Create a regular user if it doesn't exist
            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@example.com");
                user.setRoles(new HashSet<>(Collections.singletonList("USER")));
                user.setEnabled(true);
                userRepository.save(user);
                System.out.println("Regular user created");
            }

            // Add some products if the repository is empty
            if (productRepository.count() == 0) {
                Product product1 = Product.builder()
                        .name("asus zenbook 14 oled touch (um3406)")
                        .description("High-performance laptop with 16GB RAM")
                        .price(new BigDecimal("1999.99"))
                        .stockQuantity(10)
                        .imageUrl("/images/laptop.jpg")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                Product product2 = Product.builder()
                        .name("Apple iPhone 15 Pro Max")
                        .description("Latest smartphone with 1TB storage")
                        .price(new BigDecimal("1599.99"))
                        .stockQuantity(20)
                        .imageUrl("/images/smartphone.jpg")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                Product product3 = Product.builder()
                        .name("Beats Solo 4")
                        .description("Noise-cancelling wireless headphones")
                        .price(new BigDecimal("899.99"))
                        .stockQuantity(30)
                        .imageUrl("/images/headphones.jpg")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                productRepository.saveAll(Arrays.asList(product1, product2, product3));
                System.out.println("Sample products created");
            }
        };
    }
}