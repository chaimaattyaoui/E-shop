package com.ms.e_project.Services.auth;

import com.ms.e_project.dto.SignupRequest;
import com.ms.e_project.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);
}
