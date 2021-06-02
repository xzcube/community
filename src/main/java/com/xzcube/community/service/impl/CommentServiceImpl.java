package com.xzcube.community.service.impl;

import com.xzcube.community.dto.CommentShowDTO;
import com.xzcube.community.enums.CommentTypeEnum;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import com.xzcube.community.mapper.CommentMapper;
import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Comment;
import com.xzcube.community.model.Question;
import com.xzcube.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author xzcube
 * @date 2021/5/31 16:16
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired(required = false)
    QuestionMapper questionMapper;
    @Autowired(required = false)
    UserMapper userMapper;

    @Override
    @Transactional // 把整个方法体包裹在事务中
    public void insert(Comment comment) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == 0 || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType().equals(CommentTypeEnum.COMMENT.getType())){
            // 回复评论
            Comment dbComment = commentMapper.findByParentId(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else {
            // 回复问题
            Question question = questionMapper.findById(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incComment(comment.getParentId()); // 评论数+1
        }
    }

    @Override
    public List<CommentShowDTO> listByQuestionId(Integer id) {
        List<Comment> comments = commentMapper.findListByParentId(id);
        if(comments.size() == 0){
            return new ArrayList<>();
        }
        // 将所有comment封装成commentShowDTO(注意这里用到了stream api和方法引用)
        List<CommentShowDTO> commentShowDTOList = comments.stream().map(comment -> {
            CommentShowDTO commentShowDTO = new CommentShowDTO();
            BeanUtils.copyProperties(comment, commentShowDTO);
            commentShowDTO.setUser(userMapper.findById(comment.getCommentator()));
            return commentShowDTO;
        }).collect(Collectors.toList());
        return commentShowDTOList;
    }
}
