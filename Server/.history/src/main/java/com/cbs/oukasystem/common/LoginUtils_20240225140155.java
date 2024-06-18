package com.cbs.oukasystem.common;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.cbs.oukasystem.entity.login.TokenEntity;
import com.cbs.oukasystem.entity.user.UserEntity;

import lombok.Data;

@Service
@Transactional
public class LoginUtils {

    // 三日間
    private static final long EXPIRE_TIME = 3 * 24 * 60 * 60 * 1000;
    // 七日間
    private static final long REFRESH_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    // 密钥盐
    private static final String TOKEN_SECRET = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxgRV5fGqkmKXzch75Kxe";
    public static final String TYPE_TOKEN = "token";
    public static final String TYPE_REFRESH_TOKEN = "refreshToken";
    // REFRESH_TOKEN_CODE
    public static final int REFRESH_TOKEN_CODE = 2000;
    // LOGIN_LOGOUT_CODE
    public static final int LOGIN_LOGOUT_CODE = 1000;

    private static ThreadLocal<String> loginUserIdThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<UserEntity> loginUserThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<TokenEntity> loginTokenThreadLocal = new ThreadLocal<>();

    public static void initLoginUserId(String userId) {
        loginUserIdThreadLocal.set(userId);
    }

    public static String getLoginUserId() {
        return loginUserIdThreadLocal.get();
    }

    public static void removeLoginUserId() {
        loginUserIdThreadLocal.remove();
    }

    public static void initLoginUser(UserEntity user) {
        loginUserIdThreadLocal.set(user);
    }

    public static UserEntity getLoginUser() {
        return loginUserThreadLocal.get();
    }

    public static void removeLoginUser() {
        loginUserThreadLocal.remove();
    }

    public static void initLoginToken(TokenEntity token) {
        loginTokenThreadLocal.set(token);
    }

    public static TokenEntity getLoginToken() {
        return loginTokenThreadLocal.get();
    }

    public static void removeLoginToken() {
        loginTokenThreadLocal.remove();
    }

    public static String turnToken(String token) {
        return token.replace("Bearer", "").strip();
    }

    /**
     * tokenチェック
     *
     * @param token
     * @return
     */
    public static boolean verify(String token, String tokenType) {
        try {
            token = turnToken(token);
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0")
                    .withClaim("type", tokenType).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("認定成功：");
            System.out.println("userId: " + jwt.getClaim("userId").asString());
            System.out.println("type: " + jwt.getClaim("type").asString());
            System.out.println("有効時間： " + jwt.getExpiresAt());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * トークンを得る/更新する
     */
    public static TokenEntity sign(String userId, TokenEntity tokenEntity) {
        if (null == tokenEntity) {
            tokenEntity = new TokenEntity();
        }
        tokenEntity = updateToken(tokenEntity, getTempToken(userId, TYPE_TOKEN),
                getTempToken(userId, TYPE_REFRESH_TOKEN));
        return tokenEntity;
    }

    private static TokenEntity updateToken(TokenEntity tokenEntity, TempToken token,
            TempToken token_refresh) {
        tokenEntity.setToken(token.getToken());
        tokenEntity.setExpiration(token.getExpires());
        tokenEntity.setTokenRefresh(token_refresh.getToken());
        tokenEntity.setExpirationRefresh(token_refresh.getExpires());
        return tokenEntity;
    }

    /**
     * token作成
     *
     * @param userId
     * @return
     */
    private static TempToken getTempToken(String userId, String tokenType) {
        TempToken tempToken = new TempToken();
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            if (tokenType.equals(TYPE_REFRESH_TOKEN))
                expiresAt = new Date(System.currentTimeMillis() + REFRESH_EXPIRE_TIME);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userId", userId)
                    .withClaim("type", tokenType)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
            tempToken.setToken(token);
            tempToken.setExpires(expiresAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempToken;
    }
}

@Data
class TempToken {
    private String token;
    private Date expires;
}