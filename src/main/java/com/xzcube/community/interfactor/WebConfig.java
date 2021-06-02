package com.xzcube.community.interfactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xzcube
 * @date 2021/5/28 20:05
 * 配置拦截器拦截的路径
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    SessionInterceptor sessionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
       // registry.addInterceptor(loginInterceptor).addPathPatterns("/unstar/**","/profile/star","/profile/replies","/profile/questions","/publish","/wallpaper","/download/**","/profile/questions/delete/**","/thumb/**");

    }
}
