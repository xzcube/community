package com.xzcube.community.controller;

import com.xzcube.community.dto.CommentDTO;
import com.xzcube.community.dto.ResultDTO;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import com.xzcube.community.model.Question;
import com.xzcube.community.model.User;
import com.xzcube.community.service.CommentService;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.utils.HostHolder;
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
    private CommentService commentService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/delComment")
    @ResponseBody
    public Object delComment(@RequestBody CommentDTO commentDTO){
        User user = hostHolder.getUser();
        if(!commentDTO.getCreator().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.DEL_ERROR_MESSAGE);
        }
        commentService.delCommentById(commentDTO.getCommentId(), commentDTO.getParentId());
        return ResultDTO.okOf();
    }

    @PostMapping("/delQuestion")
    @ResponseBody
    public Object delQuestion(@RequestBody Question question){
        User user = hostHolder.getUser();
        if(!question.getCreator().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.DEL_ERROR_MESSAGE);
        }
        questionService.delQuestionById(question.getId());
        return ResultDTO.okOf();
    }
}
