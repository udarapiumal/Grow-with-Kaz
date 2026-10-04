package com.example.demo.Service;

import com.example.demo.Model.AdminUser;
import com.example.demo.Repository.AdminUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository repository;

    public AdminUserDetailsService(AdminUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminUser user = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("No such admin"));

        return new User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}