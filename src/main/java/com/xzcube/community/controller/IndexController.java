package com.xzcube.community.controller;

import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xzcube
 * @date 2021/5/24 22:33
 */
@Controller
public class IndexController {
    @Autowired(required = false)
    UserMapper userMapper;

    /**
     * 访问首页的时候，循环搜索所有的Cookie，找到token这个Cookie，然后通过token查询到相应的用户
     * 这样即使服务器重启也不会失去登录信息
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request){
        // 使用token获取user对象
        User user = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                break;
            }
        }
        if(user != null){
            request.getSession().setAttribute("user", user);
        }
        return "index";
    }
}
