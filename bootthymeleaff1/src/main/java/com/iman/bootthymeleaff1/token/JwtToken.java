package com.iman.bootthymeleaff1.token;

import io.jsonwebtoken.*;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken implements Serializable {
    private static final long EXPIRATION_TIME = 3 * 60 * 1000;
    /**
     * JWT SECRET KEY
     */
    private static final String SECRET = "learn to dance in the rain";

    /**
     * 簽發JWT
     */
    public String generateToken(String userEmail) {

        return Jwts.builder()
                .setSubject( userEmail )
                .setExpiration( new Date( Instant.now().toEpochMilli() + EXPIRATION_TIME  ) )
                .signWith( SignatureAlgorithm.HS512, SECRET )
                .compact();
    }

    /**
     * 驗證JWT
     */
    public void validateToken(String token) throws AuthException {
        try {
            Jwts.parser()
                    .setSigningKey( SECRET )
                    .parseClaimsJws( token );
        } catch (SignatureException e) {
            System.out.println("1");
            throw new AuthException("Invalid JWT signature.");
        }
        catch (MalformedJwtException e) {
            System.out.println("2");
            throw new AuthException("Invalid JWT token.");
        }
        catch (ExpiredJwtException e) {
            System.out.println("3");
            throw new AuthException("Expired JWT token");
        }
        catch (UnsupportedJwtException e) {
            System.out.println("4");
            throw new AuthException("Unsupported JWT token");
        }
        catch (IllegalArgumentException e) {
            System.out.println("5");
            throw new AuthException("JWT token compact of handler are invalid");
        }
    }
}
