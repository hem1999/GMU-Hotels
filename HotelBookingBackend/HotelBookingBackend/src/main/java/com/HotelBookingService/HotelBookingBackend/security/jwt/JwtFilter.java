package com.HotelBookingService.HotelBookingBackend.security.jwt;

import com.HotelBookingService.HotelBookingBackend.UserModule.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service // To mark this class as a service!
@RequiredArgsConstructor //For dependecy injection.
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService; //JwtService class implements all the functions such as validate etc;
    //We just use those functions in this class

    // Our custom implementation of UserDetailsService, that helps get the userDetails object!
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, // added @NonNull to make sure those objects are not null
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals("/auth")){
            //If the request is to signup or signin, It means, jwtToken token is not yet created
            // So we are just not doing checks in this filter and going to next!
            filterChain.doFilter(request, response);
            return ;
        }

        //request must have the token to get authenticated!
        final String authHeader = request.getHeader("Authorization");
        //We are fetching Authorization header from the request.
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            //if authorization Header doesn't have anything or
            //if it doesn't start with the string "Bearer "
            //It means not the right kind of header, so we are moving on to next filter
            filterChain.doFilter(request, response);
            return ;
        }

        //Now that we have the bearer token in the header, let's extract it and validate it.
        final String jwtToken = authHeader.substring(7); //the first 7 letters are "Bearer "
        //so we are fetching from index 7 to get only token!
        final String username = jwtService.extractUsername(jwtToken);
        //Uses a method from jwtService called extractUsername and extracts the username from token!
        if(username != null  && SecurityContextHolder.getContext().getAuthentication() == null){
            //if there is a username from our database and SecurityContextHolder doesn't have auth info,
            // then we execute this block of code!
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            //Now we have the userDetails!

            if(jwtService.isTokenValid(jwtToken, userDetails)){
                //We are using another method called `isTokenValid` which will validate our token against the userDetails
                //Now setup the UsernamePasswordAuthenticationToken with username and details
                //As you remember, in the SecurityConfig, this (JwtFilter) is added just before the
                // UsernamePasswordAuthenticationFilter.class which needs this token(UsernamePasswordAuthenticationToken) to evaluate.

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, //UserDetails
                                null, //Here credentials are null since it is JwtToken based Auth.
                                userDetails.getAuthorities() //Get Roles that are associated with the user!
                        );

                usernamePasswordAuthenticationToken.setDetails(
                        // Implementation of AuthenticationDetailsSource which builds the details object from an HttpServletRequest object,
                        // creating a WebAuthenticationDetails .

                        //AuthenticationDetailsSource ->returns a fully-configured authentication details instance
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //Now updating the SecurityContextHolder saying that our used is authenticated!
                //SecurityContextHolder is given by spring itself!
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }

        //Once the token validation and setup is done, move on to next filter
        filterChain.doFilter(request, response);

    }
}
