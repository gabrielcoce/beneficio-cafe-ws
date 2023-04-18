package com.umg.ws.bc.controllers;

import com.umg.ws.bc.exceptions.CustomExceptionHandler;
import com.umg.ws.bc.models.ERole;
import com.umg.ws.bc.models.Role;
import com.umg.ws.bc.models.User;
import com.umg.ws.bc.payload.request.LoginRequest;
import com.umg.ws.bc.payload.request.SignupRequest;
import com.umg.ws.bc.payload.response.JwtResponse;
import com.umg.ws.bc.payload.response.MessageResponse;
import com.umg.ws.bc.repository.RoleRepository;
import com.umg.ws.bc.repository.UserRepository;
import com.umg.ws.bc.security.jwt.AuthTokenFilter;
import com.umg.ws.bc.security.jwt.JwtUtils;
import com.umg.ws.bc.security.services.UserDetailsImpl;
import com.umg.ws.bc.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService){
       this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest)  {

        authService.userRegister(signUpRequest);
        /*if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }*/

        /*try {
            // Create new user's account
            User user = new User(signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();
            logger.info("strRoles " + strRoles);
            if (strRoles.isEmpty()) {
                logger.error("role esta vacio");
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: El role esta vacio."));

            } else {
                logger.info("validando role");
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "beneficio":
                            Role beneficioRole = roleRepository.findByName(ERole.ROLE_BENEFICIO)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(beneficioRole);

                            break;
                        case "agricultor":
                            Role agricultorRole = roleRepository.findByName(ERole.ROLE_AGRICULTOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(agricultorRole);

                            break;
                        case "peso":
                            Role pesoCabalRole = roleRepository.findByName(ERole.ROLE_PESO_CABAL)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(pesoCabalRole);

                            break;
                        case "user":
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);

                            break;
                        default:
                            break;
                    }
                });
            }
            user.setRoles(roles);
            logger.info("user" + user);
            if (user.getRoles().isEmpty()) {
                logger.error("rol no valido");
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Role no valido"));
            }
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            logger.error("error " + e.getMessage());
            throw new Exception("error", e.getCause());
        }*/
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
