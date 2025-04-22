package com.yatranow.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yatranow.userservice.config.CommonUtils;
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
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email is already in use");
		}

		// Encrypt the password using SHA-1
		user.setPassword(CommonUtils.encryptPassword(user.getPassword()));

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
			System.out.println("1111111");
			existingUser.setName(user.getName());
			existingUser.setEmail(user.getEmail());
			existingUser.setPhone(user.getPhone());
			existingUser.setImageUrl(user.getImageUrl());
			userRepository.save(existingUser);
			return existingUser;
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		}

	public List<User> findAllUsers() {
		
		return userRepository.findAll();
	}

}
