package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.model.DTO.UserDTO;
import com.primebuild_online.model.Role;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.Privileges;
import com.primebuild_online.model.enumerations.SignUpMethods;
import com.primebuild_online.repository.UserRepository;
import com.primebuild_online.service.RoleService;
import com.primebuild_online.service.UserService;
import com.primebuild_online.utils.validator.UserValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @Override
    public User saveUser(UserDTO userDTO) {

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new PrimeBuildException(
                    "Username already exists",
                    HttpStatus.CONFLICT
            );
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new PrimeBuildException(
                    "Email already exists",
                    HttpStatus.CONFLICT
            );
        }

        int staffCount = userRepository.countUserByRole_RoleName(
                Privileges.ADMIN.toString().toLowerCase()
        );

        if (userDTO.getRoleId() != null) {
            staffCount = userRepository.countUserByRole_RoleName(Privileges.ADMIN.toString().toLowerCase());
        }

        if (userDTO.getSignUpMethod() == null) {
            userDTO.setSignUpMethod(SignUpMethods.DIRECT);
        }

        if (staffCount == 0) {
            Role firstStaffAdmin = roleService.createFirstStaffAdmin();
            userDTO.setRoleId(firstStaffAdmin.getId());
        } else {
            throw new PrimeBuildException(
                    "Admin already exists",
                    HttpStatus.CONFLICT
            );
        }

        return userRepository.save(userSetValues(userDTO, null));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UserDTO userDTO, Long id) {
        User userInDb = userRepository.findById(id)
                .orElseThrow(() -> new PrimeBuildException(
                        "User not found",
                        HttpStatus.CONFLICT
                ));

        if (userRepository.existsByUsernameAndUserIdNot(userDTO.getUsername(), id)) {
            throw new PrimeBuildException(
                    "Username already exists",
                    HttpStatus.CONFLICT
            );
        }
        return userRepository.save(userSetValues(userDTO, userInDb));
    }


    private User userSetValues(UserDTO userDTO, User userInDb) {

        User user = new User();

        if (userInDb != null) {
            user = userInDb;
        }

        if (userDTO.getRoleId() != null) {
            Role role = roleService.getRoleById(userDTO.getRoleId());
            user.setRole(role);
        }

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getEmail() != null) {
            user.setSignUpMethod(userDTO.getSignUpMethod());
        }
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setTwoFactorEnabled(false);

        userValidator.validate(user);

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new PrimeBuildException(
                        "User not Found",
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> List.of(user))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<User> getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> List.of(user))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<User> getAllCustomers() {
        return userRepository.findAllByRole_RoleName(Privileges.CUSTOMER.toString().toLowerCase());
    }

    @Override
    public List<User> getAllStaff() {
        return userRepository.findAllByRole_RoleNameNot(Privileges.CUSTOMER.toString().toLowerCase());
    }

    @Override
    public User signupCustomer(UserDTO userDTO) {

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new PrimeBuildException(
                    "Username already exists",
                    HttpStatus.CONFLICT
            );
        }


        if (userDTO.getSignUpMethod() == null) {
            userDTO.setSignUpMethod(SignUpMethods.DIRECT);
        }

        Role customerRole = roleService.createCustomerRole();
        userDTO.setRoleId(customerRole.getId());
        userSetValues(userDTO, null);
        return userRepository.save(userSetValues(userDTO, null));
    }

    @Override
    public User createOAuth2User(String email, String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(name);
        userDTO.setEmail(email);
        userDTO.setSignUpMethod(SignUpMethods.OAUTH2);
        return signupCustomer(userDTO);
    }

}
