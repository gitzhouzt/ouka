package com.cbs.oukasystem.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class LocaleConfig {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        Locale.setDefault(Locale.CHINA);
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        String[] baseNames = { "i18n/message" };
        source.setBasenames(baseNames);
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        return source;
    }

}
