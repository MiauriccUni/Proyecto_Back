package com.mahou.mahouback.rest.auth;

import com.mahou.mahouback.logic.entity.auth.JwtService;
import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.role.RoleEnum;
import com.mahou.mahouback.logic.entity.role.RoleRepository;
import com.mahou.mahouback.logic.entity.user.GoogleUserDTO;
import com.mahou.mahouback.logic.entity.user.User;
import com.mahou.mahouback.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleUserDTO googleUserDTO, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(googleUserDTO.getEmail());

        if(optionalUser.isPresent()) {
            return new GlobalResponseHandler().handleResponse("El usuario " + googleUserDTO.getEmail() + ". ya se encuentra registrado", HttpStatus.CONFLICT, request);
        }else{
            User user = new User();
            user = new User();
            user.setEmail(googleUserDTO.getEmail());
            user.setName(googleUserDTO.getName());
            user.setLastname(googleUserDTO.getLastname());
            user.setPhoto(googleUserDTO.getPhotoUrl());
            user.setStatus(true);
            user.setPassword(passwordEncoder.encode(googleUserDTO.getEmail()));
            user.setRole(roleRepository.findByName(RoleEnum.USER).orElseThrow());
            userRepository.save(user);
        }
        return new GlobalResponseHandler().handleResponse("User registered successfully", HttpStatus.OK, request);
    }
}
