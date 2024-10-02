package com.eltosevenz.jwtdemo.config;

import com.eltosevenz.jwtdemo.filter.JwtRequestFilter;
import com.eltosevenz.jwtdemo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/authenticate", "/register", "/h2-console/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Restricted to ADMIN
                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);

        // Add JWT filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Disable frame options for H2 Console
        http.headers(AbstractHttpConfigurer::disable);

        return http.build();
    }
}