package com.chaos.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期为
    // 60 * 60 *1000  一个小时
    public static final Long JWT_TTL = 24 * 60 * 60 * 1000L;
    //设置长token密钥明文
    public static final String JWT_LONG_KEY = "LOONGKEY";
    //设置短token秘钥明文
    public static final String JWT_SHORT_KEY = "SHORTKEY";
    //长token有效期
    public static final Long LONG_JWT_TTL = 3 * 24 * 60 * 60 * 1000L;
    //短token有效期
    public static final Long SHORT_JWT_TTL = 30 * 60 * 1000L;

    public static String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * 生成jwt
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @param secretKey 密钥
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis, SecretKey secretKey) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID(), secretKey);
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid, SecretKey secretKey) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                //唯一的ID
                .setId(uuid)
                //主题  可以是JSON数据
                .setSubject(subject)
                // 签发者
                .setIssuer("xsinxcos")
                // 签发时间
                .setIssuedAt(now)
                //使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 生成加密后的秘钥 secretKey（短token）
     *
     * @return
     */
    public static SecretKey generalShortTokenKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_SHORT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 生成加密后的秘钥 secretKey（长token）
     *
     * @return
     */
    public static SecretKey generalLongTokenKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_LONG_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析短token
     *
     * @param jwt
     * @return
     */
    public static Claims parseShortToken(String jwt) {
        SecretKey secretKey = generalShortTokenKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 解析短token
     *
     * @param jwt
     * @return
     */
    public static Claims parseLongToken(String jwt) {
        SecretKey secretKey = generalLongTokenKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


    /**
     * 生成长Token
     *
     * @param subject
     * @return
     */
    public static String createLongToken(String subject) {
        return createJWT(subject, LONG_JWT_TTL, generalLongTokenKey());
    }

    /**
     * 生成短Token
     *
     * @return
     */
    public static String createShortToken(String subject) {
        return createJWT(subject, SHORT_JWT_TTL, generalShortTokenKey());
    }
}