package fi.haagahelia.cyberstore.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import fi.haagahelia.cyberstore.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                System.out.println("Initializing SecurityFilterChain");
                http
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                                .authorizeHttpRequests(authz -> {
                                        System.out.println("Configuring authorization rules");
                                        authz.requestMatchers("/", "/signup", "/css/**", "/js/**", "/images/**",
                                                        "/webjars/**", "/error", "/login")
                                                        .permitAll()
                                                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                                                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                                        .requestMatchers("/products/add", "/products/edit/**",
                                                                        "/products/delete/**")
                                                        .hasAuthority("ROLE_ADMIN")
                                                        .requestMatchers("/products").permitAll()
                                                        .requestMatchers("/cart/remove/**").authenticated()
                                                        .anyRequest().authenticated();
                                })
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/products", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .successHandler(oAuth2LoginSuccessHandler)
                                                .defaultSuccessUrl("/products", true));

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(userService)
                                .passwordEncoder(passwordEncoder)
                                .and()
                                .build();
        }
}