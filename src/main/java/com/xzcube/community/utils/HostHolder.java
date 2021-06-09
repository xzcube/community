package com.xzcube.community.utils;

import com.xzcube.community.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author xzcube
 * @date 2021/6/8 20:06
 *
 * 拥有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
