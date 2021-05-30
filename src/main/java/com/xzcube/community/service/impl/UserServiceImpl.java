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

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    /**
     * 如果数据库中没有该用户，则插入数据库，如果存在，就更新用户信息
     * @param user
     */
    @Override
    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if(dbUser == null){ // 说明数据库中没有该用户，插入该用户数据
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else { // 更新数据库信息
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
