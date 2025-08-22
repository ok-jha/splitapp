package com.ok_jha.splitapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Spring: Marks this as a configuration class
@EnableWebSecurity // Spring Security: Enables web security support
public class SecurityConfig {

    /**
     * Defines the PasswordEncoder bean used for hashing passwords.
     * Why BCryptPasswordEncoder?: It's the industry standard, strong, includes a salt automatically,
     *                          and is directly supported by Spring Security.
     * Why @Bean?: Makes the returned PasswordEncoder instance available for dependency injection
     *            throughout the application (e.g., in our UserService).
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures basic web security rules.
     * We'll expand this significantly later. For now, it sets up defaults.
     * Allows customizing things like which endpoints require authentication, form login, etc.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO: Configure actual security rules here later (e.g., permit /api/register, secure others)
        http
                .authorizeHttpRequests(authz -> authz
                        // TEMPORARY: Allowing all requests for now
                        // We WILL secure this properly later!
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable()) // TEMPORARY: Disable CSRF for stateless API (needs review for stateful)
                .httpBasic(withDefaults()); // Enable basic HTTP authentication (can be removed later for JWT)
        return http.build();
    }

}
