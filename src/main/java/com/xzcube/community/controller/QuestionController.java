package com.xzcube.community.controller;

import com.xzcube.community.dto.CommentShowDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.model.User;
import com.xzcube.community.service.CommentService;
import com.xzcube.community.service.LikeService;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xzcube
 * @date 2021/5/29 13:33
 */
@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model,
                           @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        User user = hostHolder.getUser();
        QuestionDTO questionDTO = questionService.findById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);

        PaginationDTO<CommentShowDTO> commentPage = commentService.listByQuestionId(id, pageNum);

        // 每次点击都让阅读量+1
        questionService.incView(id);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentShowDTO", commentPage);
        model.addAttribute("relatedQuestions", relatedQuestions);
        // 查询话题的点赞数
        long likeCount = likeService.findEntityLikeCount(1, id);
        model.addAttribute("likeCount", likeCount);
        if(user != null){
            int status = likeService.findEntityLikeStatus(user.getId(), 1, id);
            model.addAttribute("status", status);
        }else {
            model.addAttribute("status", 0);
        }

        return "question";
    }
}
