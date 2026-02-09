package com.primebuild_online.service;

import com.primebuild_online.model.RolePrivilege;

import java.util.List;

public interface RolePrivilegeService {
    void saveRolePrivilege(RolePrivilege rolePrivilege);

    RolePrivilege findByRoleId(Long roleId);

    void deleteAllByRoleId(Long roleId);

    void deleteById(Long id);

    List<RolePrivilege> findAllByRoleId(Long roleId);
}
