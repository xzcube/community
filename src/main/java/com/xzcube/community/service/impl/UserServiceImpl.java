package com.xzcube.community.service.impl;

import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.User;
import com.xzcube.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xzcube
 * @date 2021/5/26 22:13
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    UserMapper userMapper;
    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public User findByToken(String token) {

        return userMapper.findByToken(token);
    }
}
