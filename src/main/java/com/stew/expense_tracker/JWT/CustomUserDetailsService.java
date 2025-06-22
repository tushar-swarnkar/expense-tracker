package com.stew.expense_tracker.JWT;

import com.stew.expense_tracker.model.User;
import com.stew.expense_tracker.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByEmail(username);

        if (user.isPresent()) {
            List<SimpleGrantedAuthority> authorities = user.get().getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                    .toList();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.get().getEmail())
                    .password(user.get().getPassword())
                    .authorities(authorities)
                    .build();
        }

        throw new UsernameNotFoundException("User with username " + username + " does not exist");
    }
}
