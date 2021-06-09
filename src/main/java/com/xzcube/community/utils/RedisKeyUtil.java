package com.xzcube.community.utils;

/**
 * @author xzcube
 * @date 2021/6/8 16:03
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":"; // 通过冒号分割单词
    private static final String PRE_ENTITY_LIKE = "like:entity";

    /**
     * 生成某个实体的赞的key
     * 存储的value为set，里面存放点赞的userId 通过计数获取点赞数
     * @param entityType 实体类型  1 是话题 2 是评论
     * @param entityId 话题的id or 评论的id
     *                 生成的key :
     *                  like:entity:entityType:entityId -> set(userId)
     * @return
     */
    public static String getEntityLikeKey(int entityType, int entityId){
        return PRE_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
