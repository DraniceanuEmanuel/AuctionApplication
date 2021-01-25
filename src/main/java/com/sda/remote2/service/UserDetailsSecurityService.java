package com.sda.remote2.service;

import com.sda.remote2.model.Role;
import com.sda.remote2.model.User;
import com.sda.remote2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsSecurityService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsSecurityService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

       Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()){
            throw new UsernameNotFoundException(email);
        }

        User user = optionalUser.get();
        Set<GrantedAuthority> roles = new HashSet<>();
        Role role = user.getRole();
        roles.add(new SimpleGrantedAuthority(role.getName()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), roles);


    }
}
