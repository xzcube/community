package com.xzcube.community.dto;

import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xzcube
 * @date 2021/5/31 19:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {
    private Integer code;
    private String message;

    /**
     * 自定义错误码和错误信息
     * @return
     */
    public static ResultDTO errorOf(CustomizeErrorCode errorCode){
        return new ResultDTO(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e){
        return new ResultDTO(e.getCode(), e.getMessage());
    }

    public static ResultDTO okOf(){
        return new ResultDTO(200, "请求成功");
    }
}
