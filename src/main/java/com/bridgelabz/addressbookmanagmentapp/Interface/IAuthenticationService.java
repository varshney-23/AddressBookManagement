package com.bridgelabz.addressbookmanagmentapp.Interface;

import com.bridgelabz.addressbookmanagmentapp.DTO.AuthUserDTO;
import com.bridgelabz.addressbookmanagmentapp.DTO.LoginDTO;
import com.bridgelabz.addressbookmanagmentapp.model.AuthUser;

public interface IAuthenticationService {
    AuthUser register(AuthUserDTO userDTO) throws Exception;
    String login(LoginDTO loginDTO);
}
