package com.xzcube.community.exception;


/**
 * @author xzcube
 * @date 2021/5/31 10:47
 *
 * 自定义异常类
 */
public class CustomizeException extends RuntimeException{
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
