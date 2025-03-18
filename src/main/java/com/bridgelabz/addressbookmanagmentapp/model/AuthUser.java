package com.bridgelabz.addressbookmanagmentapp.model;


import com.bridgelabz.addressbookmanagmentapp.DTO.AuthUserDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String resetToken;


    public AuthUser(AuthUserDTO userDTO) {
        this.firstName=userDTO.getFirstName();
        this.lastName=userDTO.getLastName();
        this.email=userDTO.getEmail();
        this.password=userDTO.getPassword();
    }
}