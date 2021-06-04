package com.xzcube.community.controller;

import com.xzcube.community.dto.CommentDTO;
import com.xzcube.community.dto.CommentShowDTO;
import com.xzcube.community.dto.ResultDTO;
import com.xzcube.community.enums.CommentTypeEnum;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.model.Comment;
import com.xzcube.community.model.User;
import com.xzcube.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author xzcube
 * @date 2021/5/31 15:46
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 发布二级评论
     * @param commentDTO 对应一级评论的id
     * @param request
     * @return
     */
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");

        if(user.getId() == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if(commentDTO == null || StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        commentService.incCommentCount(commentDTO.getParentId());
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(commentDTO.getCreator());
        commentService.insert(comment, user);
        return ResultDTO.okOf();
    }

    /**
     * 展示二级评论
     * @return
     */
    @GetMapping("/comment/{id}")
    @ResponseBody
    public ResultDTO<List> comments(@PathVariable("id") Integer patentId){
        List<CommentShowDTO> commentShowDTOList = commentService.listByCommentId(patentId, CommentTypeEnum.COMMENT.getType());

        return ResultDTO.okOf(commentShowDTOList);
    }
}
