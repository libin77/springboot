package com.eltosevenz.jwtdemo.service;

import com.eltosevenz.jwtdemo.model.Actor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private Actor actor;

    public CustomUserDetails(Actor actor) {
        this.actor = actor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(actor.getRole()));
    }

    @Override
    public String getPassword() {
        return actor.getPassword();
    }

    @Override
    public String getUsername() {
        return actor.getUsername();
    }

    // Other overridden methods
}