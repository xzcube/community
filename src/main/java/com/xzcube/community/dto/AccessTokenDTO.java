package com.xzcube.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xzcube
 * @date 2021/5/25 14:43
 *
 * 获取token 封装需要接受的属性参数(gitee授权登录)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
