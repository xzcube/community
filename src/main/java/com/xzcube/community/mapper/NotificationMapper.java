package com.xzcube.community.mapper;

import com.xzcube.community.model.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xzcube
 * @date 2021/6/3 21:08
 */
@Mapper
public interface NotificationMapper {
    @Insert("insert into notification(notifier, receiver, outerId, type, gmt_create)" +
            "values(#{notifier}, #{receiver}, #{outerId}, #{type}, #{gmtCreate})")
    void insert(Notification notification);
}
