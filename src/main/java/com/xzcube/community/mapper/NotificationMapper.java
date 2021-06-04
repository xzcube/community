package com.xzcube.community.mapper;

import com.xzcube.community.model.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xzcube
 * @date 2021/6/3 21:08
 */
@Mapper
public interface NotificationMapper {
    @Insert("insert into notification(notifier, receiver, outerId, type, gmt_create)" +
            "values(#{notifier}, #{receiver}, #{outerId}, #{type}, #{gmtCreate})")
    void insert(Notification notification);

    /**
     * 根据接收者的userId查找notification
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Select("select * from notification where receiver=#{userId} limit #{offset} #{size}")
    List<Notification> listByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("size") Integer size);

    /**
     * 根据接收者的userId查询到所有未读通知的个数
     * @param userId
     * @return
     */
    @Select("select count(1) from notification where receiver=#{userId}")
    Integer countByReceiver(@Param("userId") Integer userId);
}
