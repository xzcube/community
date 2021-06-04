package com.xzcube.community.enums;

/**
 * @author xzcube
 * @date 2021/6/3 21:33
 *
 * 通知状态的枚举类（0表示未读，1表示已读）
 */
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1)
    ;
    private Integer status;

    public Integer getStatus(){
        return this.status;
    }

    NotificationStatusEnum(Integer status){
        this.status = status;
    }

}
