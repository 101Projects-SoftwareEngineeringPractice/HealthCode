package org.software.code.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class JWTUtil {
    private static final Logger logger = LogManager.getLogger(JWTUtil.class);
    private static String secretKey = "secret_key";

    public static String generateJWToken(long id, long expirationTime) {
        // 生成 JWT Token
        String qrcode_token = Jwts.builder()
                .setSubject(Long.toString(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return qrcode_token;
    }

    public static long extractID(String token) throws NullPointerException {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.error("Failed to extract ID from token: {}, error: {}", token, e.getMessage());
            throw new BusinessException(ExceptionEnum.TOKEN_EXPIRED);
        }
    }
}