package com.xzcube.community.controller;

import com.xzcube.community.dto.AccessTokenDTO;
import com.xzcube.community.model.GitHubUser;
import com.xzcube.community.model.User;
import com.xzcube.community.provider.AccessProvider;
import com.xzcube.community.service.NotificationService;
import com.xzcube.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author xzcube
 * @date 2021/5/25 14:35
 * 处理gitee的回调请求,实现gitee第三方登录
 */
@Controller
@Slf4j
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
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state", required = false) String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        // 将参数封装到AccessTokenDTO中
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState("1");
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String token = accessProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = accessProvider.getUser(token);
        if(gitHubUser != null && gitHubUser.getId() != null){
            // 将github的用户信息封装成项目需要的用户信息，然后插入数据库
            User user = new User();
            String uuid = UUID.randomUUID().toString(); // 生成一个uuid作为token，进行登录验证
            user.setToken(uuid);
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setAvatarUrl(gitHubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            if(request.getSession().getAttribute("user") == null){
                request.getSession().setAttribute("user", user);
            }
            // 将token放入cookie中
            response.addCookie(new Cookie("token", uuid));
            if(state != null){
                return "redirect:" + state;
            }
            return "redirect:/";
        }else {
            // 登录失败，重新登录
            log.error("callback get hithub error, {}", gitHubUser);
            return "redirect:/";
        }
    }

    /**
     * 退出操作
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){

        request.getSession().removeAttribute("user"); // 删除session

        // 覆盖掉原来的cookie，然后设置生存时间为0
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
