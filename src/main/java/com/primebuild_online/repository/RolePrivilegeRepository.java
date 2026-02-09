package com.primebuild_online.repository;

import com.primebuild_online.model.RolePrivilege;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    Optional<RolePrivilege> findByRoleId(Long roleId);

    @Transactional
    @Modifying
    void deleteAllByRoleId(Long roleId);

    List<RolePrivilege> findAllByRoleId(Long roleId);
}
