package com.xzcube.community.service.impl;

import com.xzcube.community.enums.CommentTypeEnum;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import com.xzcube.community.mapper.CommentMapper;
import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.model.Comment;
import com.xzcube.community.model.Question;
import com.xzcube.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

}
