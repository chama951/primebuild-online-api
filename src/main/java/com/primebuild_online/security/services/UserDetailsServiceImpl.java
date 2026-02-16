package com.primebuild_online.security.services;

import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.model.User;
import com.primebuild_online.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new PrimeBuildException(
                                "User not Found",
                                HttpStatus.NOT_FOUND));

        return UserDetailsImpl.build(user);
    }
}
