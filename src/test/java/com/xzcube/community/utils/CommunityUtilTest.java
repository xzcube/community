package com.xzcube.community.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xzcube.community.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xzcube
 * @date 2021/6/9 15:26
 */
@SpringBootTest
class CommunityUtilTest {
    @Test
    public void jsonTest(){
        User user = new User();
        user.setId(1);
        user.setName("Mary");
        user.setGmtModified(System.currentTimeMillis());
        JSONObject json = new JSONObject();
        json.put("user", user);
        System.out.println(json);
    }
}