package com.xzcube.community.service;

import com.xzcube.community.dto.NotificationDTO;
import com.xzcube.community.dto.PaginationDTO;

/**
 * @author xzcube
 * @date 2021/6/4 9:22
 */
public interface NotificationService {
    PaginationDTO<NotificationDTO> listByUserId(Integer userId, Integer offset, Integer size);
}
