package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.NotificationDTO;
import com.primebuild_online.model.Notification;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.NotificationType;
import com.primebuild_online.model.enumerations.Privileges;
import com.primebuild_online.repository.NotificationRepository;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.NotificationService;
import com.primebuild_online.service.UserService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    private User loggedInUser() {
        return userService.getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
    }

    @Override
    public void createNotification(String title,
                                   String message,
                                   NotificationType notificationType,
                                   User user) {
        Notification notification = new Notification();
        notification.setUser(loggedInUser());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setNotificationType(notificationType);
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications() {
        List<Notification> notificationList;
        if (userService.checkLoggedInIsStaff(loggedInUser())) {
            notificationList = notificationRepository.findAllByUser_Role_RoleNameNot(
                    Privileges.CUSTOMER.toString().toLowerCase());
        } else {
            notificationList = notificationRepository.findAllByUser(loggedInUser());
        }
        return notificationList;
    }

    @Override
    public void readNotificationList(NotificationDTO notificationDTO) {
        for (Notification notificationReq : notificationDTO.getNotificationList()) {
            Notification notificationInDb = getNotificationById(notificationReq.getId());
            notificationInDb.setRead(true);
            notificationRepository.save(notificationInDb);
        }
    }

    @Override
    public void deleteAllUserNotification() {
        notificationRepository.deleteAllByUser(loggedInUser());
    }

    private Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() ->
                new PrimeBuildException(
                        "Notification not Found",
                        HttpStatus.NOT_FOUND));
    }

}
