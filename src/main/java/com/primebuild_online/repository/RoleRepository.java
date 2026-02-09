package com.primebuild_online.repository;

import com.primebuild_online.model.Role;
import com.primebuild_online.model.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
