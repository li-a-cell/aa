package com.hlw.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类，提供生成和解析JWT的功能
 */
public class JwtUtils {
    /**
     * 使用 Keys.secretKeyFor 生成一个符合 HS256 要求的密钥
     */
    private static final Key signKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    /**
     * 设置JWT的过期时间
     */
    private static final Long expore = 43200000L;

    /**
     * 生成 JWT
     *
     * @param claims JWT中包含的声明，如用户信息等
     * @return 生成的JWT字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        // 使用Jwts.builder()创建JWT，并添加声明、签名密钥和过期时间
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(signKey)  // 使用生成的安全密钥
                .setExpiration(new Date(System.currentTimeMillis() + expore))
                .compact();
        return jwt;
    }

    /**
     * 解析 JWT
     *
     * @param jwt 待解析的JWT字符串
     * @return 解析后的声明对象，包含JWT中的所有声明信息
     */
    public static Claims parseJWT(String jwt) {
        // 使用相同的密钥解析JWT，并获取其中的声明信息
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)  // 使用相同的密钥解析
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}
