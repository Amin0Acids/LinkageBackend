package com.Aminoacid.linkage.controller;

import com.Aminoacid.linkage.dao.AuthenticationRequest;
import com.Aminoacid.linkage.dao.AuthenticationResponse;
import com.Aminoacid.linkage.dao.RegisterRequest;
import com.Aminoacid.linkage.dao.StateResponse;
import com.Aminoacid.linkage.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<StateResponse> signUp(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
