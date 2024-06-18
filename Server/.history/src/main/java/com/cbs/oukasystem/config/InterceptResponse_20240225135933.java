package com.cbs.oukasystem.config;

import java.util.Calendar;
import java.util.Date;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.entity.login.TokenEntity;
import com.cbs.oukasystem.vo.ResultVO;

// import com.cbs.oukasystem.common.LoginUtils;
// import com.cbs.oukasystem.entity.sys.TokenEntity;
// import com.cbs.oukasystem.vo.ResultVO;

@ControllerAdvice
public class InterceptResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter methodParameter, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        if (body instanceof ResultVO) {
            ResultVO result = (ResultVO) body;
            if (null != result) {
                if (result.getCode() != LoginUtils.LOGIN_LOGOUT_CODE) {
                    TokenEntity tokenEntity = LoginUtils.getLoginToken();
                    if (null != tokenEntity) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DATE, 1);
                        if (tokenEntity.getExpiration().compareTo(calendar.getTime()) < 1) {
                            result.setCode(LoginUtils.REFRESH_TOKEN_CODE);
                        }
                    }
                }
            }
        }
        return body;
    }
}
