package com.sda.remote2.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String email;
    private String dateOfBirth;
}
