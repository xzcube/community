package com.xzcube.community.interfactor;

import com.xzcube.community.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xzcube
 * @date 2021/6/5 20:33
 */
@Configuration
public class LoginFilterConfig {

    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilterBean(){
        FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginFilter());
        filterRegistrationBean.addUrlPatterns("/comment", "/notification/*", "/profile/replies/*", "/publish/*");
        return filterRegistrationBean;
    }
}
