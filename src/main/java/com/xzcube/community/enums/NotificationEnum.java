package com.xzcube.community.enums;

/**
 * @author xzcube
 * @date 2021/6/3 21:11
 * 消息类型的枚举类
 */
public enum  NotificationEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论"),
    Like(1, "点了赞")
    ;

    private Integer type;
    private String name;
    public Integer getType(){
        return this.type;
    }

    public String getName(){
        return this.name;
    }


    NotificationEnum(Integer type, String name){
        this.type = type;
        this.name = name;
    }

}
