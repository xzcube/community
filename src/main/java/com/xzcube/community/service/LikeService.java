package com.xzcube.community.service;

/**
 * @author xzcube
 * @date 2021/6/8 18:52
 */
public interface LikeService {
    void like(Integer userId, int entityType, Integer entityId);
    long findEntityLikeCount(int entityType, Integer entityId);
    int findEntityLikeStatus(int userId, int entityType, int entityId);

}
