package com.sparkle.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/6 上午9:51
 */
public class JWTUtil {
    //过期时间
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    //私钥
    private static final String TOKEN_SECRET = "T3u5A2k9";

    /**
     * 生成签名，15分钟过期
     *
     * @param **username**
     * @param **password**
     * @return
     */
    public static String sign(Map<String, Object> userInfo) {
        String phone = (String)userInfo.get("phone");
        String username = (String)userInfo.get("username");
        String role = (String)userInfo.get("role");
        String address = (String)userInfo.get("address");

        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("phone", phone)
                    .withClaim("username", username)
                    .withClaim("role", role)
                    .withClaim("address", address)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检验token是否正确
     *
     * @param **token**
     * @return
     */
    public static Long verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            Long userId = jwt.getClaim("userId").asLong();
            return userId;
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Map<String, String> parseJWT(String token){
        return JSONObject.parseObject(JWT.decode(token).getPayload(),Map.class);
    }

}
