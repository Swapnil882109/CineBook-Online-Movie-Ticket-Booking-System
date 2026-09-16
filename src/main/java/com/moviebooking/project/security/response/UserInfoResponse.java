package com.moviebooking.project.security.response;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserInfoResponse {

    private Long  userId;
    private String jwttoken;
    private String username;
    private List<String> roles;

    public UserInfoResponse(Long userId, String jwttoken,  List<String> roles,String username) {
        this.userId = userId;
        this.jwttoken = jwttoken;
        this.username = username;
        this.roles = roles;
    }

    public UserInfoResponse(Long userId,  List<String> roles,String username) {
        this.userId = userId;
        this.username = username;
        this.roles=roles;
    }


}
