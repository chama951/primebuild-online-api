package com.primebuild_online.repository;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildRepository extends JpaRepository<Build, Long> {
    List<Build> findAllByUser(User user);

    List<Build> findAllByUser_Role_RoleNameNot(String string);
}
