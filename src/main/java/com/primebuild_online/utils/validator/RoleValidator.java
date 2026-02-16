package com.primebuild_online.utils.validator;

import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    public void validate(Role role) {

        if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            throw new PrimeBuildException(
                    "Role name is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (role.getRolePrivilegeList() == null || role.getRolePrivilegeList().isEmpty()) {
            throw new PrimeBuildException(
                    "At least one privilege must be assigned",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
