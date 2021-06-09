package com.xzcube.community.service;

/**
 * @author xzcube
 * @date 2021/6/8 18:52
 */
public interface LikeService {
    void like(Integer userId, String entityType, Integer entityId);
    long findEntityLikeCount(String entityType, Integer entityId);
    int findEntityLikeStatus(int userId, String entityType, int entityId);

}
