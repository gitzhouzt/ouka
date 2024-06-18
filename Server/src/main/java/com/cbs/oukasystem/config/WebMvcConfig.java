package com.cbs.oukasystem.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GlobalInterceptor IntegererceptorConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(IntegererceptorConfig).addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/swagger-ui/**", "/v3/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/api/test/**")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/updateToken/**")
                .excludePathPatterns("/api/app/home/**")
                .excludePathPatterns("/api/app/login")
                .excludePathPatterns("/api/app/register")
                .excludePathPatterns("/api/app/resetPwd")
                .excludePathPatterns("/api/app/sendVerifyCode")
                .excludePathPatterns("/api/app/updateToken/**");
        // .excludePathPatterns("/api/common/initAdmin");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
        registry.addResourceHandler("/**")
                .addResourceLocations("/classpath:/META-INF/resources/",
                        "classpath:/resources/",
                        "classpath:/static/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AppLocaleResolver();
    }
}
