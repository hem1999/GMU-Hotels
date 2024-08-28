package com.HotelBookingService.HotelBookingBackend.security;

import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import com.HotelBookingService.HotelBookingBackend.UserModule.UserRepository;
import com.HotelBookingService.HotelBookingBackend.security.jwt.JwtFilter;
import com.HotelBookingService.HotelBookingBackend.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


import java.util.function.Supplier;


@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authProvider;
    private final JwtFilter jwtAuthFilter;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        //The argument is not HttpRequest Object, it is HttpSecurity Object.
        http
                .authorizeHttpRequests(
                        //authorizes requests with urls to permit all and at the end says all other requests need to be authenticated.
                        req -> req.requestMatchers("/auth/**","auth/validateToken",
                                        "/rooms/available/**","/rooms/filter","/rooms/{id}",
                                        "/feedback/avgRating","/feedback/filter"//Any customer should be able to view rooms and check availability without logging in!
                                ).permitAll()
                                .requestMatchers("/rooms/add","/rooms/update","/rooms/delete/**").hasRole("ADMIN") // making sure only  admins can do this!
                                .requestMatchers("/bookings/**").hasRole("USER")
                                .requestMatchers("/user/updateUser").authenticated()
                                .requestMatchers("/user/{id}","/user/deleteUser/{id}","/user/userBookings/{id}").access(this::hasUserId)
                                .requestMatchers("/rooms").permitAll()
                                .anyRequest().authenticated()
                )
                //Disabling CSRF
                .csrf(AbstractHttpConfigurer::disable) //For JWT based applications, it is okay to disable!
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //So that sessions are stateless and doesn't store session info in cookie
                // Every request must have it's own authorization header.
                .authenticationProvider(authProvider)
                //Setting up the authentication provider, it's a bean in the BeansConfig which
                //Implements DaoAuthentication Provider which sets the UserDetailsService that helps load the
                // the user details and also the encrypter or encoder for the password encrypt/decrypt stuff.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                //Adding the jwtFilter beforer usernamePasswordAuthenticationFilter,
        // if jwt exists in the Auth header, it will evaluate the token if exists and goes to next filter
        // otherwise it will skip jwtFilter and goes to next(usernamePasswordAuthenticationFilter)!
        return http.build();
    }



    private AuthorizationDecision hasUserId(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext requestAuthorizationContext) {

//        System.out.println("Function invoked!");
        String userId = requestAuthorizationContext.getVariables().get("id");
        UserEntity userByUrl = userRepository.findById(Long.valueOf(userId)).orElseThrow(
                () -> new UsernameNotFoundException("user not found with id: "+userId)
        );
        final String usernameByUrl = userByUrl.getUsername();
        String authHeader = requestAuthorizationContext.getRequest().getHeader("Authorization");
//        System.out.println(authHeader);
        String jwtToken = authHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
//        System.out.println("Extracted username from jwt: "+username);

        if(username.equals(usernameByUrl)) {
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }
}
