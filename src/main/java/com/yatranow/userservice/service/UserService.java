package com.yatranow.userservice.service;

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
	public Optional<User> updateUserDetails(User user) throws JsonMappingException, JsonProcessingException {
	    Long id = 0L;
	    // Extract user details from JSON
	    if (user.getId() > 0) {
	        id = user.getId();
	    } else {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required");
	    }

	    return userRepository.findById(id).map(updatedUser -> {
	        if (user.getName() != null) {
	        	updatedUser.setName(user.getName());
	        }
	        if (user.getEmail() != null) {
	        	updatedUser.setEmail(user.getEmail());
	        }
	        if (user.getRole() != null) {
	        	updatedUser.setRole(user.getRole());
	        }
	        if (user.getPhone() != null) {
	        	updatedUser.setPhone(user.getPhone());
	        }
	        return userRepository.save(updatedUser);
	    });
	}

}
