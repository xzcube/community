package com.xzcube.community.service;

import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.model.Question;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/26 22:16
 */
public interface QuestionService {
    public void create(Question question);


    PaginationDTO findAllQuestions(Integer page, Integer size);

    PaginationDTO findByCreator(Integer creator, Integer page, Integer size);
}
