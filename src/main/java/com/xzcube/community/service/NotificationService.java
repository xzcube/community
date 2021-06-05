package com.xzcube.community.service;

import com.xzcube.community.dto.NotificationDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.model.User;

/**
 * @author xzcube
 * @date 2021/6/4 9:22
 */
public interface NotificationService {
    PaginationDTO<NotificationDTO> listByUserId(Integer userId, Integer offset, Integer size);

    Integer unreadCount(Integer receiver);

    NotificationDTO setUnreadToRead(Integer id, User user);
}
