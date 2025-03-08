package com.bridgelabz.addressbookmanagmentapp.service;


import com.bridgelabz.addressbookmanagmentapp.DTO.AuthUserDTO;
import com.bridgelabz.addressbookmanagmentapp.DTO.LoginDTO;
import com.bridgelabz.addressbookmanagmentapp.Exception.UserException;
import com.bridgelabz.addressbookmanagmentapp.Interface.IAuthenticationService;
import com.bridgelabz.addressbookmanagmentapp.Repository.AuthenticationRepository;
import com.bridgelabz.addressbookmanagmentapp.model.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    AuthenticationRepository authUserRepository;

    @Override
    public AuthUser register(AuthUserDTO userDTO) throws Exception {
        AuthUser user = new AuthUser(userDTO);

        authUserRepository.save(user);

        return user;
    }
    @Override
    public String login(LoginDTO loginDTO) {
        Optional<AuthUser> user = Optional.ofNullable(authUserRepository.findByEmail(loginDTO.getEmail()));

        if (user.isPresent()) {
            return "Congratulations!! You have logged in successfully!";
        } else {
            throw new UserException("Sorry! Email or Password is incorrect!");
        }
    }
}