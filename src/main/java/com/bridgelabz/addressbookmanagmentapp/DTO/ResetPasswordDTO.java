package com.bridgelabz.addressbookmanagmentapp.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class ResetPasswordDTO {
    @NotBlank(message = "Current password cannot be empty")
    private String currentPassword;

    @NotBlank(message = "New password cannot be empty")
    private String newPassword;
}
