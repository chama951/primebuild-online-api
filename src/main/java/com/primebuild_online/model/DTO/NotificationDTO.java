package com.primebuild_online.model.DTO;

import com.primebuild_online.model.Notification;
import com.primebuild_online.model.enumerations.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    List<Notification> notificationList;
}
