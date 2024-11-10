package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.authentication.JwtUtil;
import com.spring.entity.AuthRequest;
import com.spring.entity.AuthResponse;
import com.spring.entity.User;
import com.spring.services.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

 

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
//    	user.setUsername("monster");
//    	user.setPassword("mini");
        userService.registerUser(user); // Call registerUser instead of saveUser
        
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest) {
    	System.out.println(authRequest);
        try {
            String token = userService.authenticate(authRequest);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}

