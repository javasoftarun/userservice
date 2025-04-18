package com.yatranow.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.yatranow.userservice.entity.User;
import com.yatranow.userservice.response.ApiResponse;
import com.yatranow.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "API for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registers a new user.
     *
     * @param user the user to register
     * @return the registered user
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        System.out.println("Registering user: " + user.toString());
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse("success", registeredUser, HttpStatus.OK.value()));
    }
   	
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(new ApiResponse("success", user, HttpStatus.OK.value()));
    }
	/**
	 * Deletes a user by their ID.
	 *
	 * @param id the ID of the user to delete
	 * @return a response entity indicating the result of the operation
	 */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Deletes a user by their ID")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new ApiResponse("success", null, HttpStatus.OK.value()));
    }
    
    @PatchMapping("/update")
    @Operation(summary = "Update user details", description = "Updates the user details based on JSON input")
    public ResponseEntity<ApiResponse> updateUserRole(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUserDetails(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            return ResponseEntity.ok(new ApiResponse("success", updatedUser, HttpStatus.OK.value()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON input", e);
        }
    }

}
