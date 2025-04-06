package com.pms.publicationmanagement.service.tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pms.publicationmanagement.model.user.UserTokenDetails;
import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.model.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {

    @Value("${security-config.jwt-secret}")
    private String secret;

    public String generateToken(User user) {
        Date expiry = Date.from(LocalDateTime.now().plusDays(5).atZone(ZoneId.systemDefault()).toInstant());

        Algorithm algorithm = getAlgorithm();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getName())
                .withClaim("institutionId", user.getInstitution().getId().toString())
                .withClaim("role", user.getUserRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(expiry)
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();

        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println("Invalid JWT token");
            return false;
        }

        return true;
    }

    public UserTokenDetails decodeToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String id = jwt.getSubject();
        Long institutionId = jwt.getClaim("institutionId").asLong();
        String email = jwt.getClaim("email").asString();
        String role = jwt.getClaim("role").asString();

        return new UserTokenDetails(id, institutionId, email, UserRole.valueOf(role));
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }
}
