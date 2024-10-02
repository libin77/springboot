package com.eltosevenz.jwtdemo.controller;

import com.eltosevenz.jwtdemo.model.Actor;
import com.eltosevenz.jwtdemo.repository.UserRepository;
import com.eltosevenz.jwtdemo.service.CustomUserDetailsService;
import com.eltosevenz.jwtdemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint for user login to authenticate and generate token
    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Invalid credentials", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("jwt", jwt);

        return tokenMap;
    }

    // Sample secured endpoint that can be accessed only with a valid JWT token
    @GetMapping("/hello")
    public String hello() {
        return "Hello, You have successfully authenticated with JWT!";
    }

    // Endpoint to register a new user
    @PostMapping("/register")
    public String register(@RequestBody Actor actor) {
        actor.setPassword(passwordEncoder.encode(actor.getPassword()));
        userRepository.save(actor);
        return "User registered successfully";
    }
}