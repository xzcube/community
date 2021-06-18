package com.xzcube.community.service.impl;

import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.dto.QuestionDTO;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import com.xzcube.community.mapper.CommentMapper;
import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Question;
import com.xzcube.community.model.User;
import com.xzcube.community.service.ElasticsearchService;
import com.xzcube.community.service.QuestionService;
import com.xzcube.community.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired(required = false)
    CommentMapper commentMapper;
    PaginationDTO<QuestionDTO> paginationDTO;
    @Autowired
    ElasticsearchService elasticsearchService;

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
    public PaginationDTO<QuestionDTO> findAllQuestions(String search, Integer page, Integer size) {
        Integer totalCount = questionMapper.count(); // 数据库中所有话题数量
        Integer offset = PageUtils.setOffset(page, totalCount, size);
        List<Question> questions = questionMapper.findAllQuestions(offset, size);

        return setPaginationDTO(totalCount, page, size, questions);
    }

    @Override
    public PaginationDTO<QuestionDTO> findByCreator(Integer creator, Integer page, Integer size) {

        Integer totalCount = questionMapper.countByCreator(creator); // 数据库中所有话题数量
        int offset = PageUtils.setOffset(page, totalCount, size);
        List<Question> questions = questionMapper.findByCreator(creator, offset, size);

        return setPaginationDTO(totalCount, page, size, questions);
    }

    /**
     * 通过 question 的id寻找到相应的question，然后寻找到相应的user，封装到questionDTO中
     * @param id
     * @return
     */
    @Override
    public QuestionDTO findById(Integer id) {
        Question question = questionMapper.findById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);

        // 通过question的creator寻找到相应的user
        Integer creator = question.getCreator();
        User user = userMapper.findById(creator);
        questionDTO.setUser(user);
        return questionDTO;
    }

    @Override
    public void createOrUpdate(Question question) {
        if(question.getId() == 0){ // 如果提交上来的questionId是0，则将该question插入数据库
            // 设置创建时间和修改时间
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            elasticsearchService.saveQuestion(question);
            questionMapper.create(question);
        }else {
            // 设置修改时间，更新数据库信息
            question.setGmtModified(question.getGmtCreate());
            elasticsearchService.saveQuestion(question);
            questionMapper.update(question);
        }
    }

    //每次点击都让阅读量+1
    @Override
    public void incView(Integer id) {
        questionMapper.incView(id);
    }

    @Override
    public List<Question> findHotQuestion(Integer offset, Integer count) {
        return questionMapper.findHotQuestions(offset, count);
    }

    /**
     * 根据前端传入的QuestionDTO对象中的tag属性，查找相应的话题
     * @param queryDTO QuestionDTO对象
     * @return
     */
    @Override
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), "[\\,\\，]");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));

        List<Question> questions = questionMapper.selectRelated(queryDTO.getId(), regexpTag);

        List<QuestionDTO> questionDTOList = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOList;
    }

    @Override
    @Transactional
    public void delQuestionById(Integer questionId) {
        // 寻找到以该questionId为parent_id的所有commentId，然后删除这些评论以及下面的二级评论
        List<Integer> commentIdList = commentMapper.selectByParentId(questionId);
        for (Integer id : commentIdList) {
            commentMapper.delCommentByParentId(id);
        }
        elasticsearchService.deleteQuestion(questionId);
        commentMapper.delFirstCommentByParentId(questionId);
        questionMapper.delById(questionId);
    }

    /**
     * 给 PaginationDTO对象赋值
     * @param totalCount 数据库中总话题数量
     * @param page 当前页数
     * @param size 每页展示话题数量
     * @param questions 当前展示的话题列表
     * @return
     */
    public PaginationDTO<QuestionDTO> setPaginationDTO(Integer totalCount,
                                          Integer page,
                                          Integer size,
                                          List<Question> questions){
        paginationDTO = new PaginationDTO<>();
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
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

}
