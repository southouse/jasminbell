package com.southouse.jasminbell.config;

import org.hibernate.annotations.DialectOverride;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * packageName    : com.southouse.jasminbell.config
 * fileName       : WebConfig
 * author         : southouse
 * date           : 2023/07/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/25        southouse       최초 생성
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        registry.addConverter(String.class, LocalDateTime.class, source -> {
            if (source != null && !source.isEmpty()) {
                return LocalDateTime.parse(source, formatter);
            }
            return null;
        });
    }
}

