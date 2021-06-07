package com.xzcube.community.service;

import com.xzcube.community.dto.CommentDTO;
import com.xzcube.community.dto.CommentShowDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.model.Comment;
import com.xzcube.community.model.User;

import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/31 16:15
 */
public interface CommentService {
    void insert(Comment comment, User commentator);

    PaginationDTO<CommentShowDTO> listByQuestionId(Integer id, Integer page);

    List<CommentShowDTO> listByCommentId(Integer id, Integer type);

    void incCommentCount(Integer parentId);

    void delCommentById(Integer commentId, Integer questionId);
}
