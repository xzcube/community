package com.xzcube.community.dto;

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

    private Integer notifier; // 发送通知的人
    private String notifierName; // 发送通知者的用户名
    private String outerTitle;
    private String type; // 类型的名称
}
