package com.xzcube.community.controller;

import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xzcube
 * @date 2021/5/29 13:33
 */
@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model){
        QuestionDTO questionDTO = questionService.findById(id);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("questionDTO", questionDTO);
        return "question";
    }
}
