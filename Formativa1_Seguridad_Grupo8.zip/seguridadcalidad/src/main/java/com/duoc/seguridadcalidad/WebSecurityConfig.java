package com.duoc.seguridadcalidad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/login", "/*.css", "/error").permitAll() 
                .anyRequest().authenticated() // 
)
                .formLogin((form) -> form
                .loginPage("/login") 
                .permitAll() 
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build();

        UserDetails vet = User.builder()
            .username("vet")
            .password(passwordEncoder().encode("vet123"))
            .roles("VET")
            .build();

        UserDetails asistente = User.builder()
            .username("asistente")
            .password(passwordEncoder().encode("asis123"))
            .roles("ASISTENTE")
            .build();

        return new InMemoryUserDetailsManager(admin, vet, asistente);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}