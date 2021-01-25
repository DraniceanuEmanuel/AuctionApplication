package com.sda.remote2.mapper;

import com.sda.remote2.dto.UserDto;
import com.sda.remote2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserMapper {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserMapper(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User map(UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .DateOfBirth(getDateOfBirthFrom(userDto.getDateOfBirth()))
                .build();

        return user;

    }

    private LocalDate getDateOfBirthFrom(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth);
    }
}
