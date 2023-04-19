package com.umg.ws.bc.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class JwtResponse {
    private String username;
    private String accessToken;
    /* private String tokenType = "Bearer";
     private UUID id;*/

    //private String email;
    private List<String> roles;

    public JwtResponse(String username, String accessToken, List<String> roles) {
        this.username = username;
        this.accessToken = accessToken;
        this.roles = roles;
    }

    /*public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }*/
}
