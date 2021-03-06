package com.xzcube.community.service;

import com.xzcube.community.model.User;

/**
 * @author xzcube
 * @date 2021/5/26 22:12
 */
public interface UserService {
    void insert(User user);

    User findByToken(String token);

    User findById(Integer id);

    void createOrUpdate(User user);
}
