package com.xzcube.community.controller;

import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.model.User;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * @return
     */
    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size){
        // 使用token获取user对象(在interceptor中实现了)

        // 找到所有已经发布的问题，并放入model中
        PaginationDTO pagination = questionService.findAllQuestions(page, size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
