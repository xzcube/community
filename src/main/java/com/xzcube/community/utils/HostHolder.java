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
@Data
public class HostHolder {
    ThreadLocal<User> users = new ThreadLocal<>();

    public void clear(){
        users.remove();
    }
}
