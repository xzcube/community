package com.xzcube.community.service;

import com.xzcube.community.dto.CommentDTO;
import com.xzcube.community.dto.CommentShowDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.model.Comment;

import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/31 16:15
 */
public interface CommentService {
    void insert(Comment comment);

    PaginationDTO listByQuestionId(Integer id, Integer page);

}
