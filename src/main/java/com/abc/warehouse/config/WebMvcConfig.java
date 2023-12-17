package com.abc.warehouse.config;

import com.abc.warehouse.handler.LoginInterceptor;
import com.abc.warehouse.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private LoginService loginService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> freeUri = loginService.getFreeUriList();
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(freeUri);
    }
}
