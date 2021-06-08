package com.xzcube.community.controller;

import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.model.Question;
import com.xzcube.community.model.User;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.service.UserService;
import com.xzcube.community.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xzcube
 * @date 2021/5/26 16:07
 * 跳转到发布页面 get则是渲染页面，post则是处理请求(完成发布功能)
 */
@Controller
public class PublishController {
    @Autowired(required = false)
    QuestionService questionService;
    @Autowired(required = false)
    UserService userService;
    @Autowired
    SensitiveFilter sensitiveFilter;

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("action", "发布");
        // 如果是发布问题，就将表单中的隐藏域 questionId 设置为 0
        model.addAttribute("questionId", 0);
        return "publish";
    }

    /**
     * 发布话题功能
     * @param title 标题
     * @param description 问题详情
     * @param tag 标签
     * @param questionId 数据库中的id
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "questionId", required = false) Integer questionId,
                            HttpServletRequest request,
                            Model model){
        title = sensitiveFilter.filter(title);
        description = sensitiveFilter.filter(description);
        tag = sensitiveFilter.filter(tag);
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

        //遍历cookie 找到 token 用token找到对应的用户
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
        question.setId(questionId);

        if(user == null){
            if(questionId == 0)
                model.addAttribute("action", "发布");
            else
                model.addAttribute("action", "修改");
            model.addAttribute("error", "您尚未登录");
            return "publish";
        }
        question.setCreator(user.getId()); // 将user的id作为question的creator传入

        questionService.createOrUpdate(question);
        return "redirect:/";
    }

    /**
     * 根据question的id找到相应的question，然后渲染到前端
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Integer id,
                       Model model){
        if(id == null){
            return "publish";
        }
        model.addAttribute("action", "修改");
        QuestionDTO questionDTO = questionService.findById(id);
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tag", questionDTO.getTag());
        //将question的id传入前端表单，然后在隐藏域中提交
        model.addAttribute("questionId", id);
        return "publish";
    }
}
