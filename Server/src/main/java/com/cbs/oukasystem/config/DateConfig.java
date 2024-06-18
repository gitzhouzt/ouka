package com.cbs.oukasystem.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * 日付変換
 * 
 * 
 *
 */
@Configuration
public class DateConfig {

    // フォーマット(LocalDateTime)
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    // フォーマット(LocalDate)
    public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    // フォーマット(LocalTime)
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }

    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {

                DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
                LocalDateTime date = null;
                try {
                    date = LocalDateTime.parse((String) source, df);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }

    @Bean
    public Converter<String, LocalDate> LocalDateConvert() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {

                DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
                LocalDate date = null;
                try {
                    LocalDateTime dateTime = LocalDateTime.parse((String) source, df);
                    date = dateTime.toLocalDate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }
}