package com.xzcube.community.controller;

import com.xzcube.community.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xzcube
 * @date 2021/6/8 19:25
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @PostMapping("/like")
    @ResponseBody
    public String like(Integer entityId, String entityType){
        return null;
    }
}
