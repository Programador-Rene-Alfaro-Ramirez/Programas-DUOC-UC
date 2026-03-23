package com.duoc.backend;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class Constants {

    // Spring Security
    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    public static final String ISSUER_INFO = "https://www.duocuc.cl/";
    
    // SOLUCIÓN HALLAZGO 3: Leer la clave desde variables de entorno por seguridad
    public static final String SUPER_SECRET_KEY = System.getenv("JWT_SECRET") != null ? System.getenv("JWT_SECRET") : "ClaveSecretaDeDesarrolloSuperSegura1234567890";
    
    // SOLUCIÓN HALLAZGO 4: Tiempo de expiración corregido a 1 día (86.400.000 ms)
    public static final long TOKEN_EXPIRATION_TIME = 86_400_000; // 1 day

    public static Key getSigningKeyB64(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}