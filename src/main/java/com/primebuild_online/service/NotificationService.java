package com.primebuild_online.service;

import com.primebuild_online.model.DTO.NotificationDTO;
import com.primebuild_online.model.Notification;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.NotificationType;

import java.util.List;

public interface NotificationService {
    void createNotification(String title,
                            String message,
                            NotificationType notificationType,
                            User user);

    List<Notification> getUserNotifications();

    void readNotificationList(NotificationDTO notificationDTO);

    void deleteAllUserNotification();
}
