package com.primebuild_online.service;

import com.primebuild_online.model.DTO.RoleDTO;
import com.primebuild_online.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role saveRole(RoleDTO roleDTO);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    void deleteRole(Long id);

    Role updateRole(RoleDTO roleDTO,Long id);

    Optional<Role> getRoleByName(String roleName);

    Role createFirstStaffAdmin();

    Role createCustomerRole();

    boolean checkRoleAdmin(Long roleId);
}
