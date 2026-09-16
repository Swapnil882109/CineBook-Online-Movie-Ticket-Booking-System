package com.moviebooking.project.security.jwt;

import com.moviebooking.project.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs ;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;


    // creating a cookie from jwt
    public ResponseCookie generateJwtCookies(UserDetailsImpl userPrinciple){
        String jwt=generateTokenFromUsername(userPrinciple.getUsername());
        ResponseCookie cookie=ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(true)
                .build();
        return cookie;
    }


    //creating a jwt token from username
    public String generateTokenFromUsername(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime()+jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    public Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request,jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    public boolean validateToken(String authToken){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }
        catch (MalformedJwtException e){
            System.out.println("Invalid JWT token:{}"+e.getMessage());
        }
        catch (ExpiredJwtException e){
            System.out.println("Expired JWT token:{}"+e.getMessage());
        }
        catch (UnsupportedJwtException e){
            System.out.println("Unsupported JWT token:{}"+e.getMessage());
        }
        catch (IllegalArgumentException e){
            System.out.println("JWT claims string is empty:{}"+e.getMessage());
        }

        return false;
    }

    public ResponseCookie getCleanCookie() {
        ResponseCookie cookie=ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();
        return cookie;
    }



}
