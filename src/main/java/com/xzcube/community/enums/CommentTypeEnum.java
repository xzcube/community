package com.xzcube.community.enums;

/**
 * @author xzcube
 * @date 2021/5/31 19:40
 */
public enum CommentTypeEnum {
    QUESTION(1), // 如果是question type=1
    COMMENT(2) //如果是comment type=2
    ;

    private Integer type;
    CommentTypeEnum(Integer type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }
}
