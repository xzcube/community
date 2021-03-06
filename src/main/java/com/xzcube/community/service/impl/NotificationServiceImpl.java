package com.xzcube.community.service.impl;

import com.xzcube.community.dto.NotificationDTO;
import com.xzcube.community.dto.PaginationDTO;
import com.xzcube.community.enums.NotificationEnum;
import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import com.xzcube.community.mapper.NotificationMapper;
import com.xzcube.community.mapper.UserMapper;
import com.xzcube.community.model.Notification;
import com.xzcube.community.model.User;
import com.xzcube.community.service.NotificationService;
import com.xzcube.community.utils.PageUtils;
import org.springframework.beans.BeanUtils;
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
        Integer totalCount = notificationMapper.countByReceiver(userId);
        Integer offset = PageUtils.setOffset(page, totalCount, size);
        List<Notification> notifications = notificationMapper.listByUserId(userId, offset, size);

        return setPaginationDTO(totalCount, page, size, notifications);
    }

    @Override
    public Integer unreadCount(Integer receiver) {
        return notificationMapper.unreadCount(receiver);
    }

    @Override
    public NotificationDTO setUnreadToRead(Integer id, User user) {
        Notification notification = notificationMapper.findNotificationById(id);
        if(!notification.getReceiver().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
        notificationMapper.setUnreadToRead(id);
        return notificationDTO;
    }

    public PaginationDTO<NotificationDTO> setPaginationDTO(Integer totalCount,
                                                           Integer page,
                                                           Integer size,
                                                           List<Notification> notifications){
        paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(totalCount, page, size); // ???paginationDTO??????????????????
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        if(notifications.size() == 0){
            return paginationDTO;
        }
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOList);
        return paginationDTO;
    }
}
