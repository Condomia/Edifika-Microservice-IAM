package com.edifika.iam.authentication.infrastructure.hashing.bcrypt;

import com.edifika.iam.authentication.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Interfaz que combina HashingService y PasswordEncoder para BCrypt.
 */
public interface BCryptHashingService extends HashingService, PasswordEncoder {}