package com.hiroc.blog_api.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.secret}")
    private String SECRET_KEY;
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public String extractUsername(String token){

        return extractClaim(token,Claims::getSubject); //subject -> email
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) //pass it a function of type CLAIM, and T-> type you want to return. give it a name-> claimsResolve
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims); //you get all the claims
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();


    }
    //no claims, only subject
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    //With some clams
    public String generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ){
        String token = Jwts
                .builder()
                .claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+7*24*3600))
			    .signWith(getSignInKey())
                .compact();
        log.debug("The generated JWT is {}",token);
        return token;
    }
    //Literally makes no sense to me, expiration should be enough
    public boolean isTokenValid(String token, UserDetails userDetails){
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (ExpiredJwtException e){
            //Error will be thrown by extract claims if it tries to extract from expired token;
            log.warn("Attempted to validate expired token {}",e.getMessage());
            return false;
        }
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());
    }


    public SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
