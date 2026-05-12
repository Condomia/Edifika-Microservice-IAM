package com.edifika.iam.authentication.infrastructure.tokens.jwt;

import com.edifika.iam.authentication.application.internal.outboundservices.tokens.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * Interfaz que extiende TokenService agregando soporte para Bearer tokens HTTP.
 */
public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest request);
    String generateToken(Authentication authentication);
}
