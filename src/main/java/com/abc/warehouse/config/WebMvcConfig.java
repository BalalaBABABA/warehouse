package com.abc.warehouse.config;

import com.abc.warehouse.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/resource/all",
//                        "/login",
//                        "/logout",
//                        "/user/totalpage",
//                        "/resource",
//                        "/permissiontype/**",
//                        "/user/getNamesAndIds"
//                      )
//                .addPathPatterns("/permissiontype/del/**");
//
//    }
}
