package com.eltosevenz.jwtdemo.service;

import com.eltosevenz.jwtdemo.model.Actor;
import com.eltosevenz.jwtdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Actor> user = userRepository.findByUsername(username);
        return user.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
