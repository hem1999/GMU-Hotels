package com.HotelBookingService.HotelBookingBackend.security.jwt;

import com.HotelBookingService.HotelBookingBackend.UserModule.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {


    @Value("${spring.application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${spring.app.jwtExpirationMs}")
    private Long jwtExpiration;

    public String extractUsername(String jwtToken) {
        // Claims are key,value pairs that are added to token payload,
        // Here subject is the key for the username used while constructing claims.
        // Here we are using a generic method that takes a token and function[getFunction] that is applied on that token
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken,
                               Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken); // First we are fetching all the claims as a Claims Object
        //which has the methods like getSubject, which is referred as claimsResolver in the arguments.
        //We are just applying that claimsResolver on claims and return the result.

        //Since different claims could have different types, we are using generic types such as <T>!
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()//First we are taking the jwt parser from io.jsonwebtoken.Jwts
                .verifyWith((SecretKey) getSignInKey())//The key is cast to SecretKey, suggesting it's a symmetric key used for both signing and verifying.
                .build()//building the parse
                .parseSignedClaims(jwtToken) //Then we are parsing the claims, If the signature is valid and the token hasn't expired, it returns a Jws<Claims> object.
                .getPayload();// We are getting the payload(claims)
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken); //fetcing username from token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
        //Checing if the fetched username and username from userDetails are same, then we are checking the expiry!
    }

    private boolean isTokenExpired(String jwtToken) {
        //We are using a method called extractExpiry and checking if the expiration date is before now!
        return extractExpiry(jwtToken).before(new Date());
    }

    private Date extractExpiry(String jwtToken) {
        //We are using extractClaim with getExpiration as the claimResolver!
        return extractClaim(jwtToken, Claims::getExpiration);
    }


    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //Decoding the base64 encoded secret key into bytes array
        return Keys.hmacShaKeyFor(keyBytes); //Creating a hmacShaKey from that plain secret key
    }

    /* Below Functions are related to Building JWT with claims and Generating the jwt token! */

    public String generateToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return buildJwtToken(claims, userDetails, jwtExpiration);
    }

    private String buildJwtToken(HashMap<String, Object> claims, UserDetails userDetails, Long jwtExpiration) {
        var authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities",authorities)
                .signWith(getSignInKey())
                .compact();
    }

}
