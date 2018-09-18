package com.auction.config;

import com.auction.db.model.convertor.CurrencyConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        System.out.println("bbbb");
        registry.addConverter(new CurrencyConverter());
    }
}
