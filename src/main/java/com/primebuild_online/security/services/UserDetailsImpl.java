package com.primebuild_online.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.primebuild_online.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private boolean is2faEnabled;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Collection<? extends GrantedAuthority> authorities,
                           String email, Long id, boolean is2faEnabled,
                           String password, String username) {
        this.authorities = authorities;
        this.email = email;
        this.id = id;
        this.is2faEnabled = is2faEnabled;
        this.password = password;
        this.username = username;
    }

    public static UserDetailsImpl build(User user) {

        Collection<SimpleGrantedAuthority> authorities =
                user.getRole().getRolePrivilegeList()
                        .stream()
                        .map(rp -> new SimpleGrantedAuthority(
                                rp.getPrivilege().name()
                        ))
                        .toList();

        for (SimpleGrantedAuthority SimpleGrantedAuthority : authorities) {
            System.out.println("Privileges: " + SimpleGrantedAuthority);
        }

        return new UserDetailsImpl(
                authorities,
                user.getEmail(),
                user.getUserId(),
                user.isTwoFactorEnabled(),
                user.getPassword(),
                user.getUsername()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


}