package com.primebuild_online.service;

import com.primebuild_online.model.DTO.UserDTO;
import com.primebuild_online.model.User;

import java.util.List;

public interface UserService {
    User loggedInUser();

    User saveUser(UserDTO userDTO);

    List<User> getAllUsers();

    User updateUser(UserDTO userDTO, Long id);

    User getUserById(Long id);

    void deleteUser(Long id);

    List<User> getByUsername(String username);

    List<User> getByEmail(String email);

    List<User> getAllCustomers();

    List<User> getAllStaff();

    User signupCustomer(UserDTO userDTO);

    User createOAuth2User(String email, String name);

    boolean isCustomerLoggedIn();

    boolean checkIsACustomer(User user);
}
