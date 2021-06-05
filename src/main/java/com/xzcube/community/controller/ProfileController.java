package com.xzcube.community.controller;

import com.xzcube.community.dto.NotificationDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.model.User;
import com.xzcube.community.service.NotificationService;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xzcube
 * @date 2021/5/28 9:00
 */
@Controller
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    /**
     * 处理 个人中心 请求
     * @param action 当前操作 action == questions:我的话题  action == replies:最新回复
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "userId") Integer userId,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size){

        User user = userService.findById(userId);
        model.addAttribute("userById", user); // 将根据 id 查询到的user放入model中

        if(user == null){
            return "error";
        }
        Integer unreadCount = notificationService.unreadCount(user.getId());
        if("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的话题");
            PaginationDTO<QuestionDTO> paginationDTO = questionService.findByCreator(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("unreadCount", unreadCount);
        }else if("replies".equals(action)){
            PaginationDTO<NotificationDTO> paginationDTO = notificationService.listByUserId(userId, page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("unreadCount", unreadCount); // 传入未读数
            return "notification";

        }
        return "profile";
    }
}
