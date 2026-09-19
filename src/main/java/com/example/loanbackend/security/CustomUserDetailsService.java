package com.example.loanbackend.security;

import com.example.loanbackend.model.User;
import com.example.loanbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhone())
                .password(user.getPassword() != null ? user.getPassword() : "")
                .authorities(Collections.emptyList())
                .build();
    }
}
