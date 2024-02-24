package com.example.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing
public class DataConfig {

    //❶ Enables entity auditing in Spring Data JDBC
    // ❷ Returns the currently authenticated user for auditing purposes
    // ❸ Extracts the SecurityContext object for the currently authenticated user from SecurityContextHolder
    //❹ Extracts the Authentication object for the currently authenticated user from SecurityContext
    //❺ Handles the case where a user is not authenticated, but is manipulating data. Since we protected all the endpoints, this case should never happen, but we’ll include it for completeness.
    //❻ Extracts the username for the currently authenticated user from the Authentication object
    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName);
    }
}
