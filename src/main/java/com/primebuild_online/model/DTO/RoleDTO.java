package com.primebuild_online.model.DTO;

import com.primebuild_online.model.enumerations.Privileges;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String roleName;
    private List<Privileges> privilegesList;
}
