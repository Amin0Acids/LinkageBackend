package com.Aminoacid.linkage.service;

import com.Aminoacid.linkage.dao.AuthenticationRequest;
import com.Aminoacid.linkage.dao.AuthenticationResponse;
import com.Aminoacid.linkage.dao.RegisterRequest;
import com.Aminoacid.linkage.dao.StateResponse;
import com.Aminoacid.linkage.model.User;
import com.Aminoacid.linkage.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public StateResponse register(RegisterRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(request.getRole())
                .build();
        userRepo.save(user);
        return StateResponse.builder()
                .isSuccessful(true)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );
        var user = userRepo.findByUsername(request.getUsername()).orElseThrow();
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwtToken(token)
                .role(user.getRole())
                .build();
    }
}
