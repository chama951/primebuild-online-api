package com.primebuild_online.repository;

import com.primebuild_online.model.Notification;
import com.primebuild_online.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);
    List<Notification> findAllByUser_Role_RoleNameNot(String customer);
}
