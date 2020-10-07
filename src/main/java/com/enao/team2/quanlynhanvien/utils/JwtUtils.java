package com.enao.team2.quanlynhanvien.utils;

import com.enao.team2.quanlynhanvien.exception.UnauthorizedException;
import com.enao.team2.quanlynhanvien.service.impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class JwtUtils implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("jwt");

    private final String SECRET = resourceBundle.getString("SECRET");

    private final String EXPIRATION_TIME = resourceBundle.getString("EXPIRATION_TIME");

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        long time = Long.parseLong(EXPIRATION_TIME);
        Date expiryDate = new Date(now.getTime() + time);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException | SignatureException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getUsernameFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt).getBody().getSubject();
    }
}
