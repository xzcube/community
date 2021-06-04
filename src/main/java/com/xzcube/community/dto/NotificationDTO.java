package com.xzcube.community.dto;

import com.xzcube.community.model.User;
import lombok.Data;

/**
 * @author xzcube
 * @date 2021/6/4 9:14
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;

    private User notifier; // 发送通知的人
    private String outerTitle;
    private String type;
}
