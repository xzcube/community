package com.xzcube.community.service.impl;

import com.xzcube.community.dto.PaginationDTO;
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
    PaginationDTO paginationDTO;

    @Override
    public void create(Question question) {
        questionMapper.create(question);
    }

    /**
     * 查询所有的question内容，转化为QuestionDTO并返回
     * @return
     * @param page 当前的页码
     * @param size 每页展示的话题数量
     */
    @Override
    public PaginationDTO findAllQuestions(Integer page, Integer size) {
        // 页面展示的偏移量
        int offset = size * (page - 1);
        offset = Math.max(offset, 0);

        Integer totalCount = questionMapper.count(); // 数据库中所有话题数量
        int i = 2;
        while(offset >= totalCount){
            offset = size * (page - i);
            i++;
        }
        List<Question> questions = questionMapper.findAllQuestions(offset, size);

        return setPaginationDTO(totalCount, page, size, questions);
    }

    @Override
    public PaginationDTO findByCreator(Integer creator, Integer page, Integer size) {
        // 页面展示的偏移量
        int offset = size * (page - 1);
        Integer totalCount = questionMapper.countByCreator(creator); // 数据库中所有话题数量
        int i = 2;
        while(offset >= totalCount){
            offset = size * (page - i);
            i++;
        }
        offset = Math.max(offset, 0);
        List<Question> questions = questionMapper.findByCreator(creator, offset, size);

        return setPaginationDTO(totalCount, page, size, questions);
    }

    /**
     * 给 PaginationDTO对象赋值
     * @param totalCount 数据库中总话题数量
     * @param page 当前页数
     * @param size 每页展示话题数量
     * @param questions 当前展示的话题列表
     * @return
     */
    public PaginationDTO setPaginationDTO(Integer totalCount,
                                          Integer page,
                                          Integer size,
                                          List<Question> questions){
        paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalCount, page, size); // 给paginationDTO中的属性赋值
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            // 通过question对象的creator属性查询到对应的用户（该属性和用户的id对应）
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO); // 将question对象的属性复制到questionDTO相应的属性中
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}
