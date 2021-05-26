package com.xzcube.community.provider;

import com.alibaba.fastjson.JSON;
import com.xzcube.community.dto.AccessTokenDTO;
import com.xzcube.community.model.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author xzcube
 * @date 2021/5/25 16:07
 *  向码云认证服务器发送post请求传入 用户授权码 以及 回调地址
 * 传递client_id等参数(封装在了AccessTokenDTO类中了)，获取access_token
 */
@Component
public class AccessProvider {
    /**
     * 拿code向github换token (post请求)
     * @param accessTokenDTO
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        // MediaType 决定浏览器将以什么形式、什么编码对资源进行解析
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(client));

        // 向码云认证服务器发送的post请求，传入用户授权码以及回调地址等。从accessTokenDTO中获取参数，再拼接到url中
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code&code=" + accessTokenDTO.getCode() +
                "&client_id=" +
                accessTokenDTO.getClient_id() +
                "&redirect_uri=" +
                accessTokenDTO.getRedirect_uri() +
                "&client_secret=" +
                accessTokenDTO.getClient_secret();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String tokenJson = response.body().string();
            // 以冒号分割返回的json字符串，得到的数组中第2个就是token值（带双引号，所以需要再次分割）
            String[] split = tokenJson.split(":");
            String str1 = split[1];
            String[] strings = str1.split("\"");
            return strings[1]; // 将token返回
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过get方法获取用户信息
     * @param token 传入的token
     * @return
     */
    public GitHubUser getUser(String token) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String userString = response.body().string();
            GitHubUser user = JSON.parseObject(userString, GitHubUser.class);
            return user;
        }catch (IOException e){

        }
        return null;
    }
}
