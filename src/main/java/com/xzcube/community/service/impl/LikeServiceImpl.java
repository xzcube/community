package com.xzcube.community.service.impl;

import com.xzcube.community.service.LikeService;
import com.xzcube.community.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author xzcube
 * @date 2021/6/8 18:52
 */
@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 实现点赞的业务方法
     * @param userId 点赞的人
     * @param entityType 被点赞的实体类型
     * @param entityId 被点赞实体的id
     */
    public void like(Integer userId, String entityType, Integer entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        // 判断改用户是否已经在set中，如果在，说明已经点了赞，不在的话说明没有点赞
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if(isMember){
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        }else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    /**
     * 查询实体点赞数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long findEntityLikeCount(String entityType, Integer entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    public int findEntityLikeStatus(int userId, String entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 如果userId在set中，说明已经点过赞，返回1，否则返回0
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}
