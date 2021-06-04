package com.xzcube.community.service.impl;

import com.xzcube.community.dto.NotificationDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.mapper.NotificationMapper;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Notification;
import com.xzcube.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzcube
 * @date 2021/6/4 9:23
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired(required = false)
    NotificationMapper notificationMapper;
    @Autowired(required = false)
    UserMapper userMapper;

    PaginationDTO<NotificationDTO> paginationDTO;

    @Override
    public PaginationDTO<NotificationDTO> listByUserId(Integer userId, Integer page, Integer size) {
        Integer integer = notificationMapper.countByReceiver(userId);

        List<Notification> notifications = notificationMapper.listByUserId(userId, page, size);
        Integer totalCount = notificationMapper.countByReceiver(userId);

        return null;
    }

    public PaginationDTO<NotificationDTO> setPaginationDTO(Integer totalCount,
                                                           Integer page,
                                                           Integer size,
                                                           List<Notification> notifications){
        paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(totalCount, page, size); // 给paginationDTO中的属性赋值
        List<NotificationDTO> notificationDTOList = new ArrayList<>();


        paginationDTO.setData(notificationDTOList);
        return paginationDTO;
    }
}
