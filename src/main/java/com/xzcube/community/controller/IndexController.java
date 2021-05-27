package com.xzcube.community.controller;

import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Question;
import com.xzcube.community.model.User;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/24 22:33
 */
@Controller
public class IndexController {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    /**
     * 访问首页的时候，循环搜索所有的Cookie，找到token这个Cookie，然后通过token查询到相应的用户
     * 这样即使服务器重启也不会失去登录信息
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
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
        }
        List<QuestionDTO> questionList = questionService.findAllQuestions();
        model.addAttribute("questions", questionList);
        return "index";
    }
}
