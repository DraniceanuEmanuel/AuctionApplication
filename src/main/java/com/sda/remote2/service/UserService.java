package com.sda.remote2.service;

import com.sda.remote2.dto.UserDto;
import com.sda.remote2.mapper.UserMapper;
import com.sda.remote2.model.Role;
import com.sda.remote2.model.User;
import com.sda.remote2.repository.RoleRepository;
import com.sda.remote2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;


@Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;

    }


    public void register(UserDto userDto) {
       User user = userMapper.map(userDto);
       assignRolesTo(user);
        userRepository.save(user);
    }

    public void assignRolesTo(User user){
        Role roleUser = roleRepository.findByName("ROLE_USER");
        user.setRole(roleUser);

    }
}
