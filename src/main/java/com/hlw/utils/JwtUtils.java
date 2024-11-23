package com.hlw.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    // 使用 Keys.secretKeyFor 生成一个符合 HS256 要求的密钥
    private static Key signKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static Long expore = 43200000L;  // 设置过期时间

    // 生成 JWT
    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(signKey)  // 使用生成的安全密钥
                .setExpiration(new Date(System.currentTimeMillis() + expore))
                .compact();
        return jwt;
    }

    // 解析 JWT
    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)  // 使用相同的密钥解析
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}