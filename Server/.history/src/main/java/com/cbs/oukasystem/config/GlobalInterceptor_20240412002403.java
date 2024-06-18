package com.cbs.oukasystem.config;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumLoginCheck;
import com.cbs.oukasystem.entity.login.TokenEntity;
import com.cbs.oukasystem.repository.login.TokenRepository;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenRepository tokenRepository;

    private Boolean isDev = false;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return true;
        }
        if (isDev) {
            // String userId = "11112222-3333-4444-5555-666677778888";\
            String userId = "01f91127-7c6a-4e14-8aae-fa9095ebc6a8";
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setUserId(userId);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 100);
            tokenEntity.setExpiration(calendar.getTime());
            tokenEntity.setExpirationRefresh(calendar.getTime());
            
            LoginUtils.initLoginUserId(userId);
            LoginUtils.initLoginToken(tokenEntity);
            return true;
        }
        /*
         * 検証流れ
         */
        String token = request.getHeader("Authorization");
        if (null == token) {
            throw new BaseException(EnumLoginCheck.NOT_LOGIN);
        }
        /*
         * DBの中のデータ検証
         */
        TokenEntity tokenEntity = tokenRepository.findByToken(LoginUtils.turnToken(token));
        if (null == tokenEntity) {
            throw new BaseException(EnumLoginCheck.NOT_LOGIN);
        }
        boolean isLogin = LoginUtils.verify(token, LoginUtils.TYPE_TOKEN);
        if (!isLogin) {
            String token_refresh = tokenEntity.getTokenRefresh();
            boolean isNotExpires = LoginUtils.verify(token_refresh,
                    LoginUtils.TYPE_REFRESH_TOKEN);
            if (!isNotExpires) {
                throw new BaseException(EnumLoginCheck.TOKEN_EXPIRED);
            }
        }
        LoginUtils.initLoginUserId(tokenEntity.getUserId());
        LoginUtils.initLoginToken(tokenEntity);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object o, ModelAndView mav) {

    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse rep, Object o, Exception e) {

    }

}