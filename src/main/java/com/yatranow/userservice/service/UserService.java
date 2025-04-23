package com.yatranow.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yatranow.userservice.entity.User;
import com.yatranow.userservice.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

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

		if(!user.getRole().equalsIgnoreCase("USER") && !user.getRole().equalsIgnoreCase("VENDOR")) {
			user.setPassword(user.getPassword());
		}
		if (user.getRole().equalsIgnoreCase("ADMIN")) {
			if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().length() < 8) {
				throw new IllegalArgumentException("Password is required for admin role");
			}
			user.setPassword(user.getPassword());
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

}
