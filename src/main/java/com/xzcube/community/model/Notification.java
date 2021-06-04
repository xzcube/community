package com.xzcube.community.model;

import lombok.Data;

/**
 * @author xzcube
 * @date 2021/6/3 21:18
 */
@Data
public class Notification {
    private Long id;
    private Integer notifier;
    private Integer receiver;
    private Integer outerId;
    private Integer type;
    private Long gmtCreate;
    private Integer status;
}
