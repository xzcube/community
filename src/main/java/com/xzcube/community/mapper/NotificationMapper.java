package com.xzcube.community.mapper;

import com.xzcube.community.model.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author xzcube
 * @date 2021/6/3 21:08
 */
@Mapper
public interface NotificationMapper {
    @Insert("insert into notification(notifier, receiver, outerId, type, gmt_create, notifier_name, outer_title)" +
            "values(#{notifier}, #{receiver}, #{outerId}, #{type}, #{gmtCreate}, #{notifierName}, #{outerTitle})")
    void insert(Notification notification);

    /**
     * 根据接收者的userId查找notification
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Select("select * from notification where receiver=#{userId} order by gmt_create desc limit #{offset}, #{size}")
    List<Notification> listByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("size") Integer size);

    /**
     * 根据接收者的userId查询到所有未读通知的个数
     * @param userId
     * @return
     */
    @Select("select count(1) from notification where receiver=#{userId}")
    Integer countByReceiver(@Param("userId") Integer userId);

    /**
     * 查询相关用户未读消息数量
     * @param receiver
     * @return
     */
    @Select("select count(1) from notification where receiver=#{receiver} and status=0")
    Integer unreadCount(@Param("receiver") Integer receiver);

    @Update("update notification set status=1 where id=#{id} and status=0")
    void setUnreadToRead(@Param("id")Integer id);

    @Select("select * from notification where id = #{id}")
    Notification findNotificationById(@Param("id")Integer id);
}
