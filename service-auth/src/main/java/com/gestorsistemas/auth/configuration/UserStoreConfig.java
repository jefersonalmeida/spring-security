package com.gestorsistemas.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserStoreConfig {
    /*
     * Usuários cadastrados no Authorization Server
     * exemplo de implementação em memória, mas poderia ser via banco.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        final InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(
                User.withUsername("user")
                        .password("{noop}password")
                        .roles("USER")
                        .build()
        );

        return userDetailsManager;
    }
}
