package com.xzcube.community.model;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author xzcube
 * @date 2021/5/31 16:20
 */
@Data
@Component
public class Comment {
    private Integer parentId;
    private String content;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
}
