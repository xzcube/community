package com.xzcube.community.controller;

import com.xzcube.community.dto.AccessTokenDTO;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.GitHubUser;
import com.xzcube.community.model.User;
import com.xzcube.community.provider.AccessProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author xzcube
 * @date 2021/5/25 14:35
 * 处理gitee的回调请求,实现gitee第三方登录
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
    @Autowired(required=false)
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        // 将参数封装到AccessTokenDTO中
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String token = accessProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = accessProvider.getUser(token);
        if(gitHubUser != null){
            // 将github的用户信息封装成项目需要的用户信息，然后插入数据库
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            // 登录成功，写cookie和session（通过HttpServletRequest获取session）
            request.getSession().setAttribute("user", gitHubUser);
            return "redirect:/";
        }else {
            // 登录失败，重新登录
            return "redirect:/";
        }
    }
}
