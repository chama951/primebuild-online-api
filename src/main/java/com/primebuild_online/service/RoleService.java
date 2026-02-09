package com.primebuild_online.service;

import com.primebuild_online.model.DTO.RoleDTO;
import com.primebuild_online.model.Role;

import java.util.List;

public interface RoleService {
    Role saveRole(RoleDTO roleDTO);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    void deleteRole(Long id);

    Role updateRole(RoleDTO roleDTO,Long id);
}
