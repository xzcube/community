package com.xzcube.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommunityApplication {
    @PostConstruct // 这个注解修饰的方法会在构造器调用完之后执行,所以通常作为初始化方法
    public void init(){
        // 解决netty启动冲突的问题
        // 见 Netty4Utils.setAvailableProcessors()方法
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
