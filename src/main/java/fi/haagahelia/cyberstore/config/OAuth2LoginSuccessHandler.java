package fi.haagahelia.cyberstore.config;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import fi.haagahelia.cyberstore.domain.User;
import fi.haagahelia.cyberstore.domain.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauthToken.getPrincipal();

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            // Create username from email (remove @ and everything after)
            String username = email != null ? email.split("@")[0] : "oauth2user" + System.currentTimeMillis();

            // Check if user exists
            Optional<User> existingUser = userRepository.findByUsername(username);

            if (existingUser.isEmpty()) {
                User newUser = User.builder()
                        .username(username)
                        .email(email)
                        .password("")
                        .roles(new HashSet<>(Collections.singletonList("USER")))
                        .enabled(true)
                        .build();

                userRepository.save(newUser);
                System.out.println("Created new OAuth2 user: " + username + " with role USER");
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}