package com.hiroc.blog_api.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {

//    @Value("#{security.secret}")
    private static final String SECRET_KEY = "fk";

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Key getSignInKey(){
        byte[] key_bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key_bytes);
    }
    //Apprently SecretKey is used for extracting claims now,
    // Key is depreciated with setSigningkey
    public SecretKey getSecretKey(){
        byte[] key_bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key_bytes);
    }



    public Claims extractAllClaims(String token){
        return (Claims) Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String,Object> extractClaims, UserDetails userDetails){
        return  Jwts.builder()
                    .claims()
                    .add(extractClaims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis()+1000*60+24))
                    .and()
                    .signWith(getSignInKey())
                    .compact();
    }




}
