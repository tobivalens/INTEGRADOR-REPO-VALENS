package com.example.bckndApi.util;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "Un1v3rs1d4d1c3s1";
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60 * 2; // 2 horas

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static void validateToken(String token) {
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    }

    public static void validateAuthorization(String authorization) throws InvalidAuthException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException{
        if (authorization.startsWith("Bearer ")) {
            var token = authorization.substring(7);
            JwtUtil.validateToken(token);
        }else{
            throw new InvalidAuthException("El header esta malo");
        }
    }

}