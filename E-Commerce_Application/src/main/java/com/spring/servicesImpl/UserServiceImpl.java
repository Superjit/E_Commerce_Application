package com.spring.servicesImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Service;

import com.spring.authentication.JwtUtil;
import com.spring.entity.AuthRequest;
import com.spring.entity.User;
import com.spring.repo.UserRepository;
import com.spring.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder; // Use PasswordEncoder instead of BCryptPasswordEncoder

	@Override
	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User updateUserProfile(Long userId, User user) {
		// Implement update logic
		return null;
	}

	@Override
	public Optional<User> getUserById(Long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public String authenticate(AuthRequest authRequest) {
		Optional<User> userOpt = findByUsername(authRequest.getUsername());

		// Check if user exists and password matches
		if (userOpt.isPresent() && passwordEncoder.matches(authRequest.getPassword(), userOpt.get().getPassword())) {

			// Convert User to UserDetails (if needed)
			UserDetails userDetails = new org.springframework.security.core.userdetails.User(
					userOpt.get().getUsername(), userOpt.get().getPassword(),
					AuthorityUtils.createAuthorityList(userOpt.get().getRole()) // Assuming getRole() gives the user's
																				// role(s)
			);

			// Generate JWT token
			return jwtUtil.generateToken(userDetails);
		}

		throw new RuntimeException("Invalid credentials");
	}
}
