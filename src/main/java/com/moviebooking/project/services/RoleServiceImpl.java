package com.moviebooking.project.services;


import com.moviebooking.project.exception.APIException;
import com.moviebooking.project.exception.ResourceNotFoundException;
import com.moviebooking.project.model.AppRole;
import com.moviebooking.project.model.Role;
import com.moviebooking.project.model.User;
import com.moviebooking.project.repository.RoleRepository;
import com.moviebooking.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String promoteUser(Long userId) {
        User userFromDB=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));

        Role theatreManagerRole=roleRepository.findByRoleName(AppRole.ROLE_THEATRE_MANAGER)
                .orElseThrow(()->new ResourceNotFoundException("Role","role name","ROLE_THEATRE_MANAGER"));

        if(userFromDB.getRoles().contains(theatreManagerRole)){
            throw new APIException("User is promoted theatre manager. ");
        }
        else{
            userFromDB.getRoles().add(theatreManagerRole);
        }
        userRepository.save(userFromDB);
        return "user has been promoted to theatre manager successfully.";
    }

    @Override
    public String demoteUser(Long userId) {
        User userFromDB=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
        Role theatreManagerRole=roleRepository.findByRoleName(AppRole.ROLE_THEATRE_MANAGER)
                .orElseThrow(()->new ResourceNotFoundException("Role","role name","ROLE_THEATRE_MANAGER"));
        if(!userFromDB.getRoles().contains(theatreManagerRole)){
            throw new APIException("User dosent have theatre manager role.");
        }
        else{
            userFromDB.getRoles().remove(theatreManagerRole);
        }
        userRepository.save(userFromDB);
        return "user has been demoted successfully.";
    }
}
