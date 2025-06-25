package com.minhojpa.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver() {
            {
                setDefaultLocale(Locale.KOREA);
                setCookieName("siteLang");
                setCookieMaxAge(60 * 60 * 24 * 30); // 30일(초 단위)
            }

            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                Locale locale = super.resolveLocale(request);
                System.out.println("[CookieLocaleResolver] 현재 로케일: " + locale);
                return locale;
            }
        };
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor() {
            {
                setParamName("lang"); // 여기서 파라미터 이름 명시
            }

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
                String lang = request.getParameter(getParamName());
                System.out.println("[LocaleChangeInterceptor] lang param: " + lang);
                return super.preHandle(request, response, handler);
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
