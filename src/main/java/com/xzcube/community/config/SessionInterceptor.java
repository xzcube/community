package com.xzcube.community.config;

import com.xzcube.community.model.User;
import com.xzcube.community.service.NotificationService;
import com.xzcube.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xzcube
 * @date 2021/5/28 21:39
 *
 * 配置拦截器的拦截处理
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;

    /**
     * 在处理所有请求之前进行的 获取用户信息并放入session中
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 使用token获取user对象
        User user = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userService.findByToken(token);
                    break;
                }
            }
        }
        if(user != null){
            request.getSession().setAttribute("user", user);
            Integer unreadCount = notificationService.unreadCount(user.getId());
            request.getSession().setAttribute("unreadCount", unreadCount);

        }else {
            request.getSession().setAttribute("user", new User());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
