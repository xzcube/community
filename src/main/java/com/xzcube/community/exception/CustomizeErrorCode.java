package com.xzcube.community.exception;

/**
 * @author xzcube
 * @date 2021/5/31 11:01
 *
 * 定义通用的异常信息封装
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND("你找到问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND("未选中任何问题或评论进行回复"),
    NO_LOGIN("当前操作需要登录，请登陆后重试"),
    SYS_ERROR( "服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG("评论类型错误或不存在"),
    COMMENT_NOT_FOUND("回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY("输入内容不能为空"),
    READ_NOTIFICATION_FAIL("兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND("消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL("图片上传失败"),
    INVALID_INPUT("非法输入"),
    INVALID_OPERATION("兄弟，是不是走错房间了？"),
    ;

    @Override
    public String getMessage() {
        return message;
    }

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
        //this.code = code;
    }
}
