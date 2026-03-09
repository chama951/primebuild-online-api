package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.RoleDTO;
import com.primebuild_online.model.Role;
import com.primebuild_online.model.RolePrivilege;
import com.primebuild_online.model.enumerations.Privileges;
import com.primebuild_online.repository.RolePrivilegeRepository;
import com.primebuild_online.service.RolePrivilegeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RolePrivilegeServiceImpl implements RolePrivilegeService {
    private final RolePrivilegeRepository rolePrivilegeRepository;


    public RolePrivilegeServiceImpl(RolePrivilegeRepository rolePrivilegeRepository) {
        this.rolePrivilegeRepository = rolePrivilegeRepository;
    }

    @Override
    public void saveRolePrivilege(RolePrivilege rolePrivilege) {
        rolePrivilegeRepository.save(rolePrivilege);
    }


    @Override
    public RolePrivilege findByRoleId(Long roleId) {
        Optional<RolePrivilege> rolePrivilege = rolePrivilegeRepository.findByRoleId(roleId);
        if (rolePrivilege.isPresent()) {
            return rolePrivilege.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAllByRoleId(Long roleId) {
        rolePrivilegeRepository.deleteAllByRoleId(roleId);
    }

    @Override
    public void deleteById(Long id) {
        rolePrivilegeRepository.deleteById(id);
    }

    @Override
    public List<RolePrivilege> findAllByRoleId(Long roleId) {
        return rolePrivilegeRepository.findAllByRoleId(roleId);
    }

    @Override
    public List<RolePrivilege> prepareRolePrivilegeList(RoleDTO roleDTO, Role role) {
        List<RolePrivilege> rolePrivilegeList = new ArrayList<>();
        for (Privileges privilege : roleDTO.getPrivilegesList()) {
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setRole(role);
            rolePrivilege.setPrivilege(privilege);
            rolePrivilegeList.add(rolePrivilege);
        }
        return rolePrivilegeList;
    }

}
