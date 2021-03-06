package com.xzcube.community.service;

import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.model.Question;

import java.util.List;


/**
 * @author xzcube
 * @date 2021/5/26 22:16
 */
public interface QuestionService {
    void create(Question question);


    PaginationDTO<QuestionDTO> findAllQuestions(String search, Integer page, Integer size);

    PaginationDTO<QuestionDTO> findByCreator(Integer creator, Integer page, Integer size);

    QuestionDTO findById(Integer id);

    /**
     * 如果已经存在，则修改相应的question，如果不存在，则添加该question
     * @param question
     */
    void createOrUpdate(Question question);

    void incView(Integer id);

    List<Question> findHotQuestion(Integer offset, Integer count);

    List<QuestionDTO> selectRelated(QuestionDTO questionDTO);

    void delQuestionById(Integer questionId);
}
