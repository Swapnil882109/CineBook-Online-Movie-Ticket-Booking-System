package com.moviebooking.project.security.services;

import com.moviebooking.project.security.request.LoginRequest;
import com.moviebooking.project.security.request.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    public ResponseEntity<?> signin(LoginRequest loginRequest);
    public ResponseEntity<?> signup(SignupRequest signupRequest);
    public ResponseEntity<?> logout();
    public ResponseEntity<?> getUserDetails(Authentication authentication);

    String promoteUser(Long userId, Long adminId);
}
