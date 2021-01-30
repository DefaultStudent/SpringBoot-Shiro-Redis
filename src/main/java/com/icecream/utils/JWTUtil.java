package com.icecream.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 96495
 * @Description JWT 工具类
 *
 */
public class JWTUtil {

    /**
     * 设置过期时间为 24 小时
     */
    private static final  long EXPRIE_TIME = 60 * 24 * 60 * 1000;

    /**
     * 密钥
     */
    private static final String SECRET = "SHIRO + JWT";

    /**
     * 生成 token, 5min 后过期
     *
     * @param username 用户名
     * @return 加密的token
     */
    public static String createToken(String username) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPRIE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");

            // 附带 username 信息
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    // 到期时间
                    .withExpiresAt(date)
                    // 创建一个新的 JWT，并使用给定的算法进行标记
                    .sign(algorithm);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * 验证 token 是否正确
     *
     * @param token 密钥
     * @param username 用户名
     * @return 是否正确
     */
    public static boolean verify(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            // 在 token 中附带了 username 的细腻
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();

            // 验证 token
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获得 token 中的信息，无需 secret 解密也能获得
     *
     * @param token token
     * @return token 中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException ex) {
            return null;
        }
    }
}
