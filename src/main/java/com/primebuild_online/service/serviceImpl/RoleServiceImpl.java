package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.RoleDTO;
import com.primebuild_online.model.enumerations.BuildStatus;
import com.primebuild_online.model.enumerations.Privileges;
import com.primebuild_online.repository.RoleRepository;
import com.primebuild_online.service.RolePrivilegeService;
import com.primebuild_online.service.RoleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RolePrivilegeService rolePrivilegeService;


    public RoleServiceImpl(RoleRepository roleRepository, RolePrivilegeService rolePrivilegeService) {
        this.roleRepository = roleRepository;
        this.rolePrivilegeService = rolePrivilegeService;
    }

    @Override
    public Role saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role= roleRepository.save(role);

        for(Privileges privilege: roleDTO.getPrivilegesList()){
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setRole(role);
            rolePrivilege.setPrivilege(privilege);
            rolePrivilegeService.saveRolePrivilege(rolePrivilege);
        }

        return role;
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Role Not Found"));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role updateRole(RoleDTO roleDTO,Long id) {
        Role roleInDb = roleRepository.findById(id).orElseThrow(RuntimeException::new);

        roleInDb.setRoleName(roleDTO.getRoleName());

        rolePrivilegeService.deleteAllByRoleId(id);

        for(Privileges privilege: roleDTO.getPrivilegesList()){
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setRole(roleInDb);
            rolePrivilege.setPrivilege(privilege);
            rolePrivilegeService.saveRolePrivilege(rolePrivilege);
        }

        return roleRepository.save(roleInDb);
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName.toLowerCase());
    }

}
