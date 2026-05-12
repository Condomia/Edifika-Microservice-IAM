package com.edifika.iam.authentication.infrastructure.tokens.jwt.services;

import com.edifika.iam.authentication.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.edifika.iam.authentication.infrastructure.authorization.sfs.services.UserDetailsServiceImpl;
import com.edifika.iam.authentication.infrastructure.tokens.jwt.BearerTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de tokens JWT para Edifika.
 * Genera y valida tokens de acceso para los usuarios autenticados.
 */
@Service
public class TokenServiceImpl implements BearerTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer";
    private static final int TOKEN_BEGIN_INDEX = 7;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.days:7}")
    private int tokenExpirationTimeInDays;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(String email, Long userId, List<String> roles) {
        var issuedAt = new Date();
        var expirationDate = DateUtils.addDays(issuedAt, tokenExpirationTimeInDays);
        return Jwts.builder()
                .subject(email)
                .claim("user_id", userId)
                .claim("roles", roles)
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    @Override
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return buildToken(authentication.getName(), userDetails.getId(), roles);
    }

    @Override
    public String generateToken(String email, Long userId, List<String> roles) {
        return buildToken(email, userId, roles);
    }

    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String parameter = request.getHeader(AUTHORIZATION_PARAMETER_NAME);
        if (StringUtils.hasText(parameter) && parameter.startsWith(BEARER_TOKEN_PREFIX)) {
            return parameter.substring(TOKEN_BEGIN_INDEX);
        }
        return null;
    }

    @Override
    public String getUserNameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            LOGGER.info("Token válido");
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Firma JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Token JWT malformado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Token JWT vacío: {}", e.getMessage());
        }
        return false;
    }
}
