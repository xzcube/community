package com.xzcube.community.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xzcube
 * @date 2021/6/8 21:28
 */
@SpringBootTest
class SensitiveFilterTest {
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Test
    public void testSensitiveFilter(){
        String text = "![](http://thiscommunity.oss-cn-beijing.aliyuncs.com/e31ada46-1ff4-4549-8ae6-76b75b1a1736.jpg?Expires=1962631209&OSSAccessKeyId=LTAI5tCfcy1DyhpvqFzpWWDs&Signature=MeFs1vDLn2te7pLetXl3fOcAfNU%3D)";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}