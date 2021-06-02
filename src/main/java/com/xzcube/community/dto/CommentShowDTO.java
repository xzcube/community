package com.xzcube.community.dto;

import com.xzcube.community.model.User;
import lombok.Data;

/**
 * @author xzcube
 * @date 2021/6/1 19:10
 *
 * 展示评论时的DTO
 */
@Data
public class CommentShowDTO {
    private Long id;
    private Integer parentId;
    private Integer type;
    private Long gmtCreate;
    private Long gmtModified;
    private String content;
    private Long likeCount;

    private User user;
}
