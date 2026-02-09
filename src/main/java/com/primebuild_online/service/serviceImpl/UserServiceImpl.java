package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.UserDTO;
import com.primebuild_online.model.Role;
import com.primebuild_online.model.RolePrivilege;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.Privileges;
import com.primebuild_online.repository.UserRepository;
import com.primebuild_online.service.RoleService;
import com.primebuild_online.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public User saveUser(UserDTO userDTO) {

        if (!userRepository.existsByUsername(userDTO.getUsername())) {
            userSetValues(userDTO);
        } else {
            throw new RuntimeException("Username Already Exist");
        }

        return userRepository.save(userSetValues(userDTO));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UserDTO userDTO, Long id) {
        User userInDb = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

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
        user.setCustomer(userDTO.isCustomer());
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDateTime.now().plusYears(1));
        user.setAccountExpiryDate(LocalDateTime.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        return user;
    }

    private User userSetValues(UserDTO userDTO) {

        User user = new User();

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
        user.setCustomer(userDTO.isCustomer());
        user.setPassword(passwordEncoder().encode(userDTO.getPassword()));
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDateTime.now().plusYears(1));
        user.setAccountExpiryDate(LocalDateTime.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        return user;
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User Not Found"));
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
        List<User> userList = getAllUsers();
        List<User> customerList = new ArrayList<>();
        for (User user : userList) {
            if (user.isCustomer()) {
                customerList.add(user);
            }
        }
        return customerList;
    }

    @Override
    public List<User> getAllStaff() {
        List<User> userList = getAllUsers();
        List<User> staffList = new ArrayList<>();
        for (User user : userList) {
            if (!user.isCustomer()) {
                staffList.add(user);
            }
        }
        return staffList;
    }
}
