package com.cbs.oukasystem.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class AppLocaleResolver implements LocaleResolver {

    private static final String DEFAULT_LANG = "zh_CN";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader("Accept-Language");
        if (lang == null || lang.length() == 0) {
            lang = request.getParameter("lang");
        }
        if (lang == null || lang.length() == 0) {
            lang = DEFAULT_LANG;
        }
        String[] s = lang.split("_");
        if (s.length == 2) {
            return new Locale(s[0], s[1]);
        } else {
            return new Locale(s[0]);
        }
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Locale locale) {

    }

}
