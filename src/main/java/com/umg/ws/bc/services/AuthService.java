package com.umg.ws.bc.services;


import com.umg.ws.bc.exceptions.CustomExceptionHandler;
import com.umg.ws.bc.models.Role;
import com.umg.ws.bc.models.User;
import com.umg.ws.bc.payload.request.SignupRequest;
import com.umg.ws.bc.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void userRegister(SignupRequest signUpRequest) {
        /*if (userRepository.existsByUsername(signUpRequest.getUsername())) {

        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {

        }*/
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        user.setRoles(roles);
        logger.info("new user " + user);
        userRepository.save(user);
    }
}
