package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.RoleDTO;
import com.primebuild_online.model.enumerations.Privileges;
import com.primebuild_online.repository.RoleRepository;
import com.primebuild_online.service.RolePrivilegeService;
import com.primebuild_online.service.RoleService;
import com.primebuild_online.utils.validator.RoleValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RolePrivilegeService rolePrivilegeService;
    private final RoleValidator roleValidator;


    public RoleServiceImpl(RoleRepository roleRepository, RolePrivilegeService rolePrivilegeService, RoleValidator roleValidator) {
        this.roleRepository = roleRepository;
        this.rolePrivilegeService = rolePrivilegeService;
        this.roleValidator = roleValidator;
    }

    @Override
    public Role saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());

        if (roleRepository.existsByRoleNameIgnoreCase(role.getRoleName())) {
            throw new PrimeBuildException(
                    "Role already exists",
                    HttpStatus.CONFLICT
            );
        }

        List<RolePrivilege> rolePrivilegeList =
                rolePrivilegeService.prepareRolePrivilegeList(roleDTO, role);

        role.setRolePrivilegeList(rolePrivilegeList);

        roleValidator.validate(role);

        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new PrimeBuildException(
                        "Role not found",
                        HttpStatus.NOT_FOUND));
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
    public Role updateRole(RoleDTO roleDTO, Long id) {
        Role roleInDb = roleRepository.findById(id).orElseThrow(
                () -> new PrimeBuildException(
                        "Role not found",
                        HttpStatus.NOT_FOUND));

        roleInDb.setRoleName(roleDTO.getRoleName());

        if (roleRepository.existsByRoleNameAndIdNot(roleInDb.getRoleName(), id)) {
            throw new PrimeBuildException(
                    "Role already exists",
                    HttpStatus.CONFLICT
            );
        }

        roleInDb.getRolePrivilegeList().clear();

        List<RolePrivilege> rolePrivilegeList =
                rolePrivilegeService.prepareRolePrivilegeList(roleDTO, roleInDb);

        roleInDb.getRolePrivilegeList().addAll(rolePrivilegeList);

        roleValidator.validate(roleInDb);

        return roleRepository.save(roleInDb);
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName.toLowerCase());
    }

    @Override
    public Role createFirstStaffAdmin() {
        List<Privileges> privileges = new ArrayList<>();
        privileges.add(Privileges.ADMIN);

        String adminrRoleName = Privileges.ADMIN.toString().toLowerCase();

        Role roleInDb = getRoleByName(adminrRoleName)
                .orElseGet(() -> saveRole(prepareRole(
                        adminrRoleName,
                        privileges)));
        return roleInDb;
    }

    @Override
    public Role createCustomerRole() {
        List<Privileges> privileges = new ArrayList<>();
        privileges.add(Privileges.CUSTOMER);

        String customerRoleName = Privileges.CUSTOMER.toString().toLowerCase();

        return getRoleByName(customerRoleName)
                .orElseGet(() -> saveRole(prepareRole(
                        customerRoleName,
                        privileges)));
    }

    private RoleDTO prepareRole(String roleName, List<Privileges> privileges) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(roleName);
        roleDTO.setPrivilegesList(privileges);
        return roleDTO;
    }
}
