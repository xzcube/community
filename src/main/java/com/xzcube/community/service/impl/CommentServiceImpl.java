package com.xzcube.community.service.impl;

import com.xzcube.community.dto.CommentShowDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.enums.CommentTypeEnum;
import com.xzcube.community.enums.NotificationEnum;
import com.xzcube.community.enums.NotificationStatusEnum;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import com.xzcube.community.mapper.CommentMapper;
import com.xzcube.community.mapper.NotificationMapper;
import com.xzcube.community.mapper.QuestionMapper;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Comment;
import com.xzcube.community.model.Notification;
import com.xzcube.community.model.Question;
import com.xzcube.community.model.User;
import com.xzcube.community.service.CommentService;
import com.xzcube.community.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired(required = false)
    NotificationMapper notificationMapper;
    Integer size = 4;

    @Override
    @Transactional // 把整个方法体包裹在事务中
    public void insert(Comment comment, User commentator) {
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
            // 获得相关问题的对象
            Question question = questionMapper.findById(dbComment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            questionMapper.incComment(comment.getParentId()); // 评论数+1
            createNotify(comment, dbComment.getCommentator(), commentator.getName(),
                    question.getTitle(), NotificationEnum.REPLY_COMMENT, question.getId());

        }else {
            // 回复问题
            Question question = questionMapper.findById(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incComment(comment.getParentId()); // 评论数+1
            createNotify(comment, question.getCreator(), commentator.getName(),
                    question.getTitle(), NotificationEnum.REPLY_QUESTION, comment.getParentId());
        }
    }

    /**
     * 向数据库中插入一条通知信息
     * @param comment 通知内容的对象
     * @param receiver 接收通知者的id
     * @param notifierName 发送通知者的用户名
     * @param notificationType 通知类型
     * @param outerTitle 被回复的话题的标题，如果是回复评论，则是该评论所在话题的标题
     * @param outerId 无论是一级评论还是二级评论，传入的都是相应话题的id
     */
    public void createNotify(Comment comment, Integer receiver,
                             String notifierName, String outerTitle,
                             NotificationEnum notificationType,
                             Integer outerId){
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setOuterId(outerId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setType(notificationType.getType());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    @Override
    public PaginationDTO<CommentShowDTO> listByQuestionId(Integer id, Integer page) {
        Integer totalCount = commentMapper.commentCount(id); // 数据库中所有话题数量
        Integer offset = PageUtils.setOffset(page, totalCount, size);

        List<Comment> commentList = commentMapper.findByQuestionId(id, offset);
        PaginationDTO<CommentShowDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(totalCount, page, size);
        List<CommentShowDTO> commentShowDTOList = commentList.stream().map(comment -> {
            CommentShowDTO commentShowDTO = new CommentShowDTO();
            BeanUtils.copyProperties(comment, commentShowDTO);
            commentShowDTO.setUser(userMapper.findById(comment.getCommentator()));
            return commentShowDTO;
        }).collect(Collectors.toList());
        paginationDTO.setData(commentShowDTOList);
        return paginationDTO;
    }

    /**
     * 获取二级评论
     * @param id
     * @return
     */
    @Override
    public List<CommentShowDTO> listByCommentId(Integer id, Integer type) {
        List<Comment> commentList = commentMapper.findByCommentId(id, type);
        List<CommentShowDTO> commentShowDTOList = commentList.stream().map(comment -> {
           CommentShowDTO commentShowDTO = new CommentShowDTO();
           BeanUtils.copyProperties(comment, commentShowDTO);
           commentShowDTO.setUser(userMapper.findById(comment.getCommentator()));
           return commentShowDTO;
        }).collect(Collectors.toList());
        return commentShowDTOList;
    }

    @Override
    public void incCommentCount(Integer parentId) {
        commentMapper.incCommentId(parentId);
    }

}
