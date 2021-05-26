package com.xzcube.community.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xzcube
 * @date 2021/5/25 17:07
 * 封装github用户的类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GitHubUser {
    private Integer id;
    private String name;
    private String email;
    private String avatarUrl;
}
