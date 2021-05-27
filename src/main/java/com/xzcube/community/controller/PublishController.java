package com.xzcube.community.controller;

import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Question;
import com.xzcube.community.model.User;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xzcube
 * @date 2021/5/26 16:07
 * 跳转到发布页面 get则是渲染页面，post则是处理请求
 */
@Controller
public class PublishController {
    @Autowired(required = false)
    QuestionService questionService;
    @Autowired(required = false)
    UserService userService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            HttpServletRequest request,
                            Model model){
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        if(title == null || title.equals("")){
            model.addAttribute("error", "请填写标题");
            return "publish";
        }
        if(description == null || "".equals(description)){
            model.addAttribute("error", "问题描述不能为空");
            return "publish";
        }
        if(tag == null || "".equals(tag)){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
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
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);

        if(user == null){
            model.addAttribute("error", "您尚未登录");
            return "publish";
        }
        question.setCreator(user.getId()); // 将user的id作为question的creator传入
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        questionService.create(question);
        return "redirect:/";
    }
}
