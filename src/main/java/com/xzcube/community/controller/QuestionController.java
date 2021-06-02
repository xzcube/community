package com.xzcube.community.controller;

import com.xzcube.community.dto.CommentShowDTO;
import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.service.CommentService;
import com.xzcube.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/29 13:33
 */
@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model){
        QuestionDTO questionDTO = questionService.findById(id);
        List<CommentShowDTO> commentShowDTOList = commentService.listByQuestionId(id);

        // 每次点击都让阅读量+1
        questionService.incView(id);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentShowDTO", commentShowDTOList);

        return "question";
    }
}
