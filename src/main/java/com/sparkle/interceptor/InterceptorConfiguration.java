package com.sparkle.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "interceptor-config")
public class InterceptorConfiguration implements WebMvcConfigurer {

    private List<String> loginInterceptorExcludePath;

    public List<String> getLoginInterceptorExcludePath() {
        return loginInterceptorExcludePath;
    }

    public void setLoginInterceptorExcludePath(List<String> loginInterceptorExcludePath) {
        this.loginInterceptorExcludePath = loginInterceptorExcludePath;
    }

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(loginInterceptorExcludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
