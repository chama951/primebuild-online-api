package com.primebuild_online.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String jwtToken;

    private String username;
    private List<String> roles;
    private String redirectUrl;

    public LoginResponseDTO(String username,
                            List<String> roles,
                            String jwtToken,
                            String redirectUrl) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.redirectUrl = redirectUrl;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

}
