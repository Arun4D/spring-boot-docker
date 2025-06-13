package com.springboot.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .ignoringRequestMatchers(new AntPathRequestMatcher("/api/**")))
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/actuator/health")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/actuator/info")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).hasRole("ADMIN")
                .anyRequest().authenticated())
            .httpBasic(withDefaults());

        return http.build();
    }
}
