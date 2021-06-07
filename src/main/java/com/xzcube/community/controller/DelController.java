package com.xzcube.community.controller;

import com.xzcube.community.dto.CommentDTO;
import com.xzcube.community.dto.ResultDTO;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import com.xzcube.community.model.User;
import com.xzcube.community.service.CommentService;
import com.xzcube.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xzcube
 * @date 2021/6/7 21:36
 * 删除评论或者话题
 */
@Controller
public class DelController {
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;

    @PostMapping("/delComment")
    @ResponseBody
    public Object delComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(!commentDTO.getCreator().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.DEL_ERROR_MESSAGE);
        }
        commentService.delCommentById(commentDTO.getCommentId(), commentDTO.getParentId());
        return ResultDTO.okOf();
    }

    @GetMapping("/delQuestion")
    public String delQuestion(@RequestParam(name = "questionId", defaultValue = "0") Integer questionId,
                              @RequestParam(name = "creator", defaultValue = "0") Integer creator,
                              HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(!creator.equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.DEL_ERROR_MESSAGE);
        }
        questionService.delQuestionById(questionId);
        return "redirect:/";
    }
}
