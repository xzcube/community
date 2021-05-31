package com.xzcube.community.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author xzcube
 * @date 2021/5/31 16:08
 */
@Component
@Data
public class CommentDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
