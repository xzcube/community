package com.xzcube.community.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xzcube
 * @date 2021/5/25 17:07
 * 封装从github获取到的用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubUser {
    private Integer id;
    private String name;
    private String email;
    private String avatarUrl;
}
