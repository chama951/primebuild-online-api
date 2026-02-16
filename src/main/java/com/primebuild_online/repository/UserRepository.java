package com.primebuild_online.repository;

import com.primebuild_online.model.Role;
import com.primebuild_online.model.RolePrivilege;
import com.primebuild_online.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsernameAndUserIdNot(String username, Long userId);

    int countUserByRole_RoleName(String admin);

    boolean existsByEmail(String email);

    List<User> findAllByRole_RoleName(String roleName);

    List<User> findAllByRole_RoleNameNot(String roleName);
}
