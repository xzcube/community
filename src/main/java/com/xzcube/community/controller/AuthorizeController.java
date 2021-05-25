package com.xzcube.community.controller;

import com.xzcube.community.dto.AccessTokenDTO;
import com.xzcube.community.model.GitHubUser;
import com.xzcube.community.provider.AccessProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xzcube
 * @date 2021/5/25 14:35
 * 处理gitee的回调请求
 */
@Controller
public class AuthorizeController {
    @Autowired
    private AccessProvider accessProvider;
    @Autowired
    private AccessTokenDTO accessTokenDTO;
    @Value("${github.client.id}") // 从配置文件中读取该配置
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        // 将参数封装到AccessTokenDTO中
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String token = accessProvider.getAccessToken(accessTokenDTO);
        GitHubUser user = accessProvider.getUser(token);
        return "index";
    }
}
