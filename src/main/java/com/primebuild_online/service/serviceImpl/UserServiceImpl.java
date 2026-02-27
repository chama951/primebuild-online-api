package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.RolePrivilege;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.*;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.model.DTO.UserDTO;
import com.primebuild_online.model.Role;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.Privileges;
import com.primebuild_online.model.enumerations.SignUpMethods;
import com.primebuild_online.repository.UserRepository;
import com.primebuild_online.utils.validator.UserValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final BuildService buildService;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           UserValidator userValidator,
                           @Lazy PasswordEncoder passwordEncoder,
                           BuildService buildService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.buildService = buildService;
    }

    @Override
    public User loggedInUser() {
        return getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
    }


    @Override
    public User createAdminUser(UserDTO userDTO) {

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

        Long adminCount = getAdminCount();

        if (userDTO.getSignUpMethod() == null) {
            userDTO.setSignUpMethod(SignUpMethods.DIRECT);
        }

        if (adminCount == 0) {
            Role adminRole = roleService.createAdminRole();
            userDTO.setRoleId(adminRole.getId());
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

        if (userDTO.getEmail() != null) {
            user.setSignUpMethod(userDTO.getSignUpMethod());
        }

        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
        User user = getUserById(id);
        if (buildService.userBuildsIsExists(user)) {
            throw new PrimeBuildException(
                    "User cannot be remove while found in Builds",
                    HttpStatus.CONFLICT);
        }
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
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().getRolePrivilegeList()
                        .stream()
                        .anyMatch(rp -> rp.getPrivilege() == Privileges.CUSTOMER))
                .toList();
    }

    @Override
    public List<User> getAllStaff() {
        Set<Privileges> staffPrivileges = Set.of(
                Privileges.ADMIN,
                Privileges.USER_MANAGEMENT,
                Privileges.INVENTORY_MANAGEMENT,
                Privileges.BUILD_MANAGEMENT,
                Privileges.INVOICE_MANAGEMENT
        );

        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() != null
                        && user.getRole().getRolePrivilegeList() != null
                        && user.getRole().getRolePrivilegeList()
                        .stream()
                        .anyMatch(rp -> staffPrivileges.contains(rp.getPrivilege())))
                .toList();
    }

    @Override
    public User signupCustomer(UserDTO userDTO) {

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

        if (userDTO.getSignUpMethod() == null) {
            userDTO.setSignUpMethod(SignUpMethods.DIRECT);
        }

        Role customerRole = roleService.createCustomerRole();
        userDTO.setRoleId(customerRole.getId());

        return userRepository.save(userSetValues(userDTO, null));
    }

    @Override
    public User createOAuth2User(String email, String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(name.replaceAll("\\s+", ""));
        userDTO.setEmail(email);
        userDTO.setSignUpMethod(SignUpMethods.OAUTH2);
        return signupCustomer(userDTO);
    }


    @Override
    public boolean isCustomerLoggedIn() {
        boolean isCustomer = false;
        List<RolePrivilege> privileges = loggedInUser().getRole().getRolePrivilegeList();
        if (privileges.size() == 1 && privileges.getFirst().getPrivilege() == Privileges.CUSTOMER) {
            isCustomer = true;
        }
        return isCustomer;
    }

    @Override
    public boolean checkIsACustomer(User user) {
        boolean isCustomer = false;
        List<RolePrivilege> privileges = user.getRole().getRolePrivilegeList();
        if (privileges.size() == 1 && privileges.getFirst().getPrivilege() == Privileges.CUSTOMER) {
            isCustomer = true;
        }
        return isCustomer;
    }

    private Long getAdminCount() {
        Set<Privileges> adminPrivilege = Set.of(Privileges.ADMIN);

        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() != null
                        && u.getRole().getRolePrivilegeList() != null
                        && u.getRole().getRolePrivilegeList()
                        .stream()
                        .anyMatch(rp -> adminPrivilege.contains(rp.getPrivilege())))
                .count();
    }

}
