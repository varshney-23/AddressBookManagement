package com.bridgelabz.addressbookmanagmentapp.service;


import com.bridgelabz.addressbookmanagmentapp.DTO.AuthUserDTO;
import com.bridgelabz.addressbookmanagmentapp.DTO.LoginDTO;
import com.bridgelabz.addressbookmanagmentapp.Exception.UserException;
import com.bridgelabz.addressbookmanagmentapp.Interface.IAuthenticationService;
import com.bridgelabz.addressbookmanagmentapp.Repository.AuthenticationRepository;
import com.bridgelabz.addressbookmanagmentapp.Util.EmailSenderService;
import com.bridgelabz.addressbookmanagmentapp.model.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    AuthenticationRepository authUserRepository;

    @Autowired
    EmailSenderService emailSenderService;

    PasswordEncoder passwordEncoder;

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

    @Override
    public String forgotPassword(String email, String newPassword) {
        AuthUser user = authUserRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Sorry! We cannot find the user email: " + email);
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        authUserRepository.save(user);

        emailSenderService.sendEmail(user.getEmail(),
                "Password Forget updation Successful",
                "Hi " + user.getFirstName() + ",\n\nYour password has been successfully changed!");

        return "Password has been changed successfully!";
    }
    @Override
    public String resetPassword(String email, String currentPassword, String newPassword) {
        AuthUser user = authUserRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserException("Current password is incorrect!");
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        authUserRepository.save(user);

        emailSenderService.sendEmail(user.getEmail(),
                "Password Reset Successful",
                "Hi " + user.getFirstName() + ",\n\nYour password has been successfully updated!");

        return "Password reset successfully!";
    }

}