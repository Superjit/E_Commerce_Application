package com.spring.services;

import java.util.Optional;

import com.spring.entity.AuthRequest;
import com.spring.entity.User;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    User updateUserProfile(Long userId, User user);
    Optional<User> getUserById(Long userId);
    String authenticate(AuthRequest authRequest);
}

