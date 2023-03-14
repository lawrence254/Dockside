package com.dockside.customers.security;

import java.security.Key;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {
    private static final String SECRET_KEY= "635166546A576D5A7134743777217A25432A462D4A614E645267556B58703272";

    /**
     * Get the user name(email) from the submitted token
     * @param token
     * @return String
     */
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject); 
    };

    /**
     * An abstract method to extract claims from the provided JWT token
     * @param <T>
     * @param token
     * @param claimsResolver
     * @return
     */

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }
    /**
     * generateToken(UserDetails userDetails)
     * Generate a token without the extra claims
     * 
     * @return token
     */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * generateToken(Map<String, Object>extraClaims, UserDetails userDetails)
     * Generate a token containing extra claims
     * 
     * @return token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }


   /**
    * 
    * @param token
    * @param userDetails
    * @return boolean
    */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    
    /**
     * Check if the JWT Token is expired
     * @param token
     * @return boolean
     */
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Return date when JWT token is expected to expired
     * @param token
     * @return Date
     */
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 
     * @param token
     * @return claims
     */

    private Claims extractClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * This method return the key that is used to sign the Token
     * @return Key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
