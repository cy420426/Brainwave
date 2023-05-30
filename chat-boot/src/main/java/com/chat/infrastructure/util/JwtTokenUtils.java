package com.chat.infrastructure.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chat.infrastructure.po.UmsMember;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenUtils {
    /**
     * token的请求头
     */
    public final static String TOKEN_HEADER = "AccessToken";
    /**
     * token前缀
     */
    public final static String TOKEN_PREFIX = "Bearer ";
    /**
     * 默认密钥
     */
    private final static String DEFAULT_SECRET = "#$^!RGZDkash(123)&%%^@";
    /**
     * 过期时间
     */
    public final static long TOKEN_EXPIRATION = 604800 * 1000;
    /**
     * 加密的算法
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(DEFAULT_SECRET);

    private final static SerializeConfig CONFIG;

    static {
        CONFIG = new SerializeConfig();
        CONFIG.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    private static final SerializerFeature[] FEATURES = {
            // 输出空置字段
            SerializerFeature.WriteMapNullValue,
            // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullListAsEmpty,
            // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullNumberAsZero,
            // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
            // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteNullStringAsEmpty
    };

    public static long getRedisTokenExpiration(){
        return TOKEN_EXPIRATION;
    }

    /**
     * 登录
     *
     * @return .
     */
    public static String createToken(UmsMember user) {
        Map<String, Object> headerClaims = new HashMap<>(15);
        headerClaims.put("typ", "ZIP");
        return JWT.create()
                .withClaim("token", JSON.toJSONString(user, CONFIG, FEATURES))
                .withHeader(headerClaims)
                //主题
                .withSubject(user.getId().toString())
                .withIssuer(user.getOpenid())
                //签发时间
                .withIssuedAt(generateCurrentDate())
                //过期时间
                .withExpiresAt(generateExpirationDate())
                //签名信息，采用secret作为私钥
                .sign(ALGORITHM);
    }


    /**
     * 检验token是否正确
     *
     * @param token .
     * @return 用户id
     */
    public static String parseToken(String token) {
        String userId;
        try {
            userId = getJWTFromToken(token).getSubject();
        } catch (Exception e) {
            userId = null;
        }
        return userId;

    }


    /**
     * 验证是否token过期
     *
     * @param token
     * @return
     */
    public static boolean isVerify(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            // 如果token失效了，verify就会出现异常
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * @param token
     * @return
     */
    public static DecodedJWT getJWTFromToken(String token) {
        DecodedJWT jwt;
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            jwt = verifier.verify(token);
            return jwt;
        } catch (Exception ex) {
            jwt = null;
        }
        return jwt;
    }


    /**
     * 从请求头中获取token
     *
     * @param request
     * @return
     */
    public static String getJwtToken(HttpServletRequest request) {
        // 如果cookie中没有，就去header里面获取
        String header = request.getHeader(TOKEN_HEADER);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        //去掉token prefix
        return header.split(" ")[1].trim();
    }


    /**
     * 从请求头中获取token
     *
     * @param token
     * @return
     */
    public static UmsMember getJwtTokenLoginUser(String token) {
        try {
            DecodedJWT jwtFromToken = JwtTokenUtils.getJWTFromToken(token);
            if (jwtFromToken == null) {
                return null;
            }
            Claim claim = jwtFromToken.getClaim("token");
            String asString = claim.asString();
            return JSONObject.parseObject(asString, UmsMember.class);
        } catch (Exception ex) {
            log.error("用户token转换失败..{}",ex.getMessage());
            return null;
        }
    }


    /**
     * 当前时间
     *
     * @return
     */
    private static Date generateCurrentDate() {
        return new Date();
    }

    /**
     * 过期时间
     *
     * @return
     */
    private static Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + TOKEN_EXPIRATION);
    }
}
