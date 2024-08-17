package com.HotelBookingService.HotelBookingBackend.security;

import com.HotelBookingService.HotelBookingBackend.UserModule.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration //To make this class recognized by spring!
// Indicates that a class declares one or more @Bean methods and
// may be processed by the Spring container to generate bean definitions and service requests
// for those beans at runtime
@RequiredArgsConstructor //This is to decrease boilerplate from lombok,
//This will create a constructor taking all the final variables as arguments and initializes it.
//which is the way for dependency injection.
public class BeansConfig {

    // We have our own CustomUserDetatilsService that implements UserDetailsService!
    private final CustomUserDetailsService customUserDetailsService;

    @Bean //Made it as a bean, so that it's lifecycle is managed by spring, and also it makes the Injection Easy!
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider; //AuthenticationProvider is an Interface that is implemented by many classes
        //one of them is DaoAuthenticationProvider!.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Bcrypt encryption is the safest available.
        return new BCryptPasswordEncoder();
    }

    //To allow CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("localhost:4200/**","http://localhost:4200","localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
