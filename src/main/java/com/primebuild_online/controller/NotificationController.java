package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.NotificationDTO;
import com.primebuild_online.model.Notification;
import com.primebuild_online.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getUserNotifications() {
        return notificationService.getUserNotifications();
    }

    @PostMapping
    public void readNotificationList(@RequestBody NotificationDTO notificationDTO) {
        notificationService.readNotificationList(notificationDTO);
    }

    @DeleteMapping
    public void deleteAllUserNotification(){
        notificationService.deleteAllUserNotification();
    }
}

