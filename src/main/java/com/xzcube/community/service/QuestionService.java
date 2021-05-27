package com.xzcube.community.service;

import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.model.Question;

import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/26 22:16
 */
public interface QuestionService {
    public void create(Question question);


    List<QuestionDTO> findAllQuestions();
}
