package com.moviebooking.project.security.services;


import com.moviebooking.project.exception.ResourceNotFoundException;
import com.moviebooking.project.model.User;
import com.moviebooking.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUserName(username)
                .orElseThrow( ()-> new ResourceNotFoundException("User","username",username));
        return UserDetailsImpl.build(user);
    }
}
