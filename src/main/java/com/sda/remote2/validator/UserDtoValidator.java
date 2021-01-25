package com.sda.remote2.validator;

import com.sda.remote2.dto.UserDto;
import com.sda.remote2.model.User;
import com.sda.remote2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
public class UserDtoValidator {

    private UserRepository userRepository;

    @Autowired
    public UserDtoValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void validate(UserDto userDto, BindingResult bindingResult) {

         Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
         if(optionalUser.isPresent()){
             bindingResult.addError(new FieldError("userDto", "email", "This email is in use!"));
         }

         if(!userDto.getPassword().equals(userDto.getConfirmPassword())){
            bindingResult.addError(new FieldError("userDto","confirmPassword", "Passwords does not match !"));
        }
    }
}
