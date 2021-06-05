package com.xzcube.community.controller;

import com.xzcube.community.dto.NotificationDTO;
import com.xzcube.community.enums.NotificationEnum;
import com.xzcube.community.model.User;
import com.xzcube.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xzcube
 * @date 2021/6/4 20:56
 */
@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable("id") Integer id){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.setUnreadToRead(id, user);
        if(NotificationEnum.REPLY_COMMENT.getType().equals(notificationDTO.getType())
        || NotificationEnum.REPLY_QUESTION.getType().equals(notificationDTO.getType())){
            return "redirect:/question/" + notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }
    }
}
