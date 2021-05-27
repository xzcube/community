package com.xzcube.community.service.impl;

import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Question;
import com.xzcube.community.model.User;
import com.xzcube.community.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/26 22:16
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired(required = false)
    QuestionMapper questionMapper;
    @Autowired(required = false)
    UserMapper userMapper;
    @Override
    public void create(Question question) {
        questionMapper.create(question);
    }

    /**
     * 查询所有的question内容，转化为QuestionDTO并返回
     * @return
     */
    @Override
    public List<QuestionDTO> findAllQuestions() {
        List<Question> questions = questionMapper.findAllQuestions();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            // 通过question对象的creator属性查询到对应的用户（该属性和用户的id对应）
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO); // 将question对象的属性复制到questionDTO相应的属性中
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
