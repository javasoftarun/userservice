package com.yatranow.userservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yatranow.userservice.entity.User;
import com.yatranow.userservice.repository.UserRepository;
import com.yatranow.userservice.request.LoginRequest;
import com.yatranow.userservice.response.LoginResponse;
import com.yatranow.userservice.response.TokenResponse;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Value("${token.api.url}")
	String tokenApiUrl;

	/**
	 * Registers a new user.
	 *
	 * @param user the user to register
	 * @return the registered user
	 */
	public User registerUser(User user) {
		if (userRepository.existsByPhone(user.getPhone())) {
			throw new IllegalArgumentException("Mobile is already in use");
		}
		if (user.getRole().equalsIgnoreCase("ADMIN")) {
			if (userRepository.existsByEmail(user.getEmail())) {
				throw new IllegalArgumentException("Email is already in use");
			}
			if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() < 8) {
				throw new IllegalArgumentException("Password is required for Admin role");
			}
		}

		return userRepository.save(user);
	}

	/**
	 * Retrieves a user by their ID.
	 *
	 * @param id the ID of the user
	 * @return an Optional containing the user if found, or empty if not found
	 */
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	
	/**
	 * Deletes a user by their ID.
	 *
	 * @param id the ID of the user to delete
	 * @throws ResponseStatusException if the user is not found
	 */
	public void deleteUserById(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		userRepository.deleteById(id);
	}

	/**
	 * Updates user details.
	 *
	 * @param user the user with updated details
	 * @return the updated user
	 * @throws JsonMappingException if there is an error mapping JSON to the User object
	 * @throws JsonProcessingException if there is an error processing JSON
	 */
	public User updateUserDetails(User user) throws JsonMappingException, JsonProcessingException {
	    
		return userRepository.findById(user.getId()).map(existingUser -> {
			existingUser.setName(user.getName());
			existingUser.setEmail(user.getEmail());
			existingUser.setImageUrl(user.getImageUrl());
			userRepository.save(existingUser);
			return existingUser;
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		}

	public List<User> findAllUsers() {
		
		return userRepository.findAll();
	}

	public Optional<User> getUserByMobile(String mobile) {
		if (!userRepository.existsByPhone(mobile)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		return userRepository.findByPhone(mobile);
	}
	
	public LoginResponse loginAndFetchToken(LoginRequest loginRequest) {
		Map<String, String> tokenRequest = new HashMap<>();
		LoginResponse loginResponse = new LoginResponse();
        // Validate username/email and password
        User user = validateUserCredentials(loginRequest.getUsername(), loginRequest.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        // Get the user's mobile number
        String mobileNumber = user.getPhone();

        // Call external API to get the token
        RestTemplate restTemplate = new RestTemplate();
        tokenRequest.put("mobile", mobileNumber);
        ResponseEntity<TokenResponse> tokenResponse = restTemplate.postForEntity(tokenApiUrl, tokenRequest, TokenResponse.class);

        if (tokenResponse.getStatusCode() == HttpStatus.OK && tokenResponse.getBody() != null) {
        	loginResponse.setUserId(user.getId());
        	loginResponse.setName(user.getName());
        	loginResponse.setEmail(user.getEmail());
        	loginResponse.setPhone(user.getPhone());
        	loginResponse.setRole(user.getRole());
        	loginResponse.setToken(tokenResponse.getBody().getToken());
            return loginResponse;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve token");
        }
    }

    public Optional<User> validateUserCredentials(String username, String password) {
        // Implement validation logic (e.g., check database for user credentials)
        return userRepository.findByUsernameAndPassword(username, password);
    }

}
