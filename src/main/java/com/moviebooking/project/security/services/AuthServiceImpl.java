package com.moviebooking.project.security.services;


import com.moviebooking.project.exception.APIException;
import com.moviebooking.project.model.AppRole;
import com.moviebooking.project.model.Role;
import com.moviebooking.project.model.User;
import com.moviebooking.project.repository.RoleRepository;
import com.moviebooking.project.repository.UserRepository;
import com.moviebooking.project.security.jwt.JwtUtils;
import com.moviebooking.project.security.request.LoginRequest;
import com.moviebooking.project.security.request.SignupRequest;
import com.moviebooking.project.security.response.MessageResponse;
import com.moviebooking.project.security.response.UserInfoResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;


    @Override
    public ResponseEntity<?> signin(LoginRequest loginRequest) {
        Authentication authentication ;
        try{
            authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            Map<String,Object> map = new HashMap<>();
            map.put("message","BAD_CREDENTIALS");
            map.put("status","false");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie=jwtUtils.generateJwtCookies(userDetails);
        List<String> roles=userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        UserInfoResponse loginResponse=new UserInfoResponse(userDetails.getId(),jwtCookie.toString(),roles,userDetails.getUsername());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(loginResponse);
    }

    @Override
    public ResponseEntity<?> signup(@RequestBody SignupRequest  signupRequest) {
        if(signupRequest.getRole().contains(AppRole.ROLE_THEATRE_MANAGER)){
            throw new APIException("Theatre Manager is not allowed to sign up");
        }
        if(userRepository.existsByUserName(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error:username is already taken"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error:Account with this email already exists"));
        }

        User user=new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword())
        );

        Set<String> strRoles=signupRequest.getRole();
        Set<Role> roles=new HashSet<>();

        if(strRoles==null){
            Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        }
        else{
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Role adminRole=roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @Override
    public ResponseEntity<?> logout() {

        ResponseCookie cookie=jwtUtils.getCleanCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new  MessageResponse("you have been signed out"));

    }

    @Override
    public ResponseEntity<?> getUserDetails(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles=userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        UserInfoResponse loginResponse=new UserInfoResponse(userDetails.getId(), roles,userDetails.getUsername());
        return ResponseEntity.ok().body(loginResponse);
    }

    @Override
    public String promoteUser(Long userId, Long adminId) {
        return "";
    }
}
