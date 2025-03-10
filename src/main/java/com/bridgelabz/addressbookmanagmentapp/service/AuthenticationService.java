package com.bridgelabz.addressbookmanagmentapp.service;


import com.bridgelabz.addressbookmanagmentapp.DTO.AuthUserDTO;
import com.bridgelabz.addressbookmanagmentapp.DTO.LoginDTO;
import com.bridgelabz.addressbookmanagmentapp.Exception.UserException;
import com.bridgelabz.addressbookmanagmentapp.Interface.IAuthenticationService;
import com.bridgelabz.addressbookmanagmentapp.Repository.AuthenticationRepository;
import com.bridgelabz.addressbookmanagmentapp.Util.EmailSenderService;
import com.bridgelabz.addressbookmanagmentapp.Util.jwttoken;
import com.bridgelabz.addressbookmanagmentapp.model.AuthUser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    AuthenticationRepository authUserRepository;

    @Autowired
    jwttoken tokenUtil;

    @Autowired
    EmailSenderService emailSenderService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthUser register(AuthUserDTO userDTO) throws Exception {
        try {
            log.info("Registering new user: {}", userDTO.getEmail());
            AuthUser user = new AuthUser(userDTO);

            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);

            String token = tokenUtil.createToken(user.getUserId());
            authUserRepository.save(user);

            emailSenderService.sendEmail(
                    user.getEmail(),
                    "Registered in Greeting App",
                    "Hi " + user.getFirstName() + ",\nYou have been successfully registered!\n\nYour registered details are:\n\n"
                            + "User Id: " + user.getUserId() + "\n"
                            + "First Name: " + user.getFirstName() + "\n"
                            + "Last Name: " + user.getLastName() + "\n"
                            + "Email: " + user.getEmail() + "\n"
                            + "Token: " + token);

            log.info("User {} registered successfully.", user.getEmail());
            return user;

        } catch (Exception e) {
            log.error("Error occurred while registering user: {}", e.getMessage());
            throw new UserException("Registration failed due to an internal error. Please try again.");
        }
    }

    @Override
    public String login(LoginDTO loginDTO) {
        try {
            log.info("Login attempt for email: {}", loginDTO.getEmail());
            Optional<AuthUser> user = Optional.ofNullable(authUserRepository.findByEmail(loginDTO.getEmail()));

            if (user.isPresent()) {
                if (passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
                    log.info("Login successful for user: {}", user.get().getEmail());
                    emailSenderService.sendEmail(
                            user.get().getEmail(),
                            "Logged in Successfully!",
                            "Hi " + user.get().getFirstName() + ",\n\nYou have successfully logged in into Greeting App!");

                    return "Congratulations!! You have logged in successfully!";
                } else {
                    log.warn("Login failed: Incorrect password for email: {}", loginDTO.getEmail());
                    throw new UserException("Sorry! Email or Password is incorrect!");
                }
            } else {
                log.warn("Login failed: No user found for email: {}", loginDTO.getEmail());
                throw new UserException("Sorry! Email or Password is incorrect!");
            }

        } catch (Exception e) {
            log.error("Error during login process: {}", e.getMessage());
            throw new UserException("Login failed due to an internal error. Please try again.");
        }
    }

    @Override
    public String forgotPassword(String email, String newPassword) {
        try {
            log.info("Processing forgot password request for email: {}", email);
            AuthUser user = authUserRepository.findByEmail(email);

            if (user == null) {
                log.warn("Forgot password request failed: No user found for email: {}", email);
                throw new UserException("Sorry! We cannot find the user email: " + email);
            }

            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            authUserRepository.save(user);

            emailSenderService.sendEmail(
                    user.getEmail(),
                    "Password Forget Updation Successful",
                    "Hi " + user.getFirstName() + ",\n\nYour password has been successfully changed!");

            log.info("Password updated successfully for email: {}", email);

            return "Password has been changed successfully!";
        } catch (Exception e) {
            log.error("Error during forgot password process: {}", e.getMessage());
            throw new UserException("Error occurred while updating password. Please try again.");
        }
    }

    @Override
    public String resetPassword(String email, String currentPassword, String newPassword) {
        try {
            log.info("Resetting password for email: {}", email);
            AuthUser user = authUserRepository.findByEmail(email);

            if (user == null) {
                log.warn("Password reset failed: No user found for email: {}", email);
                throw new UserException("User not found with email: " + email);
            }

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                log.warn("Password reset failed: Incorrect current password for email: {}", email);
                throw new UserException("Current password is incorrect!");
            }

            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            authUserRepository.save(user);

            emailSenderService.sendEmail(
                    user.getEmail(),
                    "Password Reset Successful",
                    "Hi " + user.getFirstName() + ",\n\nYour password has been successfully updated!");

            log.info("Password reset successful for email: {}", email);

            return "Password reset successfully!";
        } catch (Exception e) {
            log.error("Error during password reset process: {}", e.getMessage());
            throw new UserException("Error occurred while resetting password. Please try again.");
        }
    }
}