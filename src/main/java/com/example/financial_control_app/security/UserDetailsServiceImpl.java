package com.example.financial_control_app.security;

import com.example.financial_control_app.user.UserModel;
import com.example.financial_control_app.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository repo) {
        this.userRepository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(user.getId(),
                user.getAccount() != null ? user.getAccount().getId() : null,
                user.getUsername(),
                user.getPasswordHash(),
                authorities);
    }
}
