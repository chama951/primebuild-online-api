package com.primebuild_online.repository;

import com.primebuild_online.model.Role;
import com.primebuild_online.model.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String lowerCase);

    boolean existsByRoleNameIgnoreCase(String trim);

    boolean existsByRoleNameAndIdNot(String roleName, Long id);
}
