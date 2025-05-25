package com.yatranow.userservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.yatranow.userservice.request.LoginRequest;
import com.yatranow.userservice.response.ApiResponse;
import com.yatranow.userservice.response.LoginResponse;
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
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Validated User user) {
        System.out.println("Registering user: " + user.toString());
        User registeredUser = userService.registerUser(user);
        if (registeredUser != null) {
        	try {
        		userService.sendNotification(registeredUser);
			} catch (Exception e) {
				return ResponseEntity.ok(new ApiResponse("success", new Object[] {registeredUser}, HttpStatus.OK.value()));
			}
        	
		}
        return ResponseEntity.ok(new ApiResponse("success", new Object[] {registeredUser}, HttpStatus.OK.value()));
    }
   	
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(new ApiResponse("success", new Object[] { user }, HttpStatus.OK.value()));
    }
    
    @GetMapping("/all")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> user = ( userService.findAllUsers());
        return ResponseEntity.ok(new ApiResponse("success", user.toArray(), HttpStatus.OK.value()));
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
    public ResponseEntity<ApiResponse> updateUser(@RequestBody @Validated User user) {
        try {
            User updatedUser = userService.updateUserDetails(user);

            return ResponseEntity.ok(new ApiResponse("success", new Object[] {updatedUser}, HttpStatus.OK.value()));
        } catch (Exception e) {
        	return ResponseEntity.status(500).body(new ApiResponse(e.getMessage(), null, 500));
        }
    }
    
    
    @PatchMapping("/update/password")
    @Operation(summary = "Update user password", description = "Updates the user password based on JSON input")
    public ResponseEntity<ApiResponse> updateUserPassword(@RequestBody Map<String, String> request) {
        try {
            String response = userService.updateUserPassword(request);

            return ResponseEntity.ok(new ApiResponse("success", new Object[] {response}, HttpStatus.OK.value()));
        } catch (Exception e) {
        	return ResponseEntity.status(500).body(new ApiResponse(e.getMessage(), null, 500));
        }
    }
    
    @GetMapping("/find/{mobile}")
    @Operation(summary = "Get user by mobile", description = "Retrieves a user by their mobile number")
    public ResponseEntity<ApiResponse> getUserByMobile(@PathVariable("mobile") String mobile) {
		User user = userService.getUserByMobile(mobile)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return ResponseEntity.ok(new ApiResponse("success", new Object[] { user }, HttpStatus.OK.value()));
    }
    
    @GetMapping("/find/email/{email}")
    @Operation(summary = "Get user by mobile", description = "Retrieves a user by their email")
    public ResponseEntity<ApiResponse> getUserByEmail(@PathVariable("email") String email) {
		User user = userService.getUserByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return ResponseEntity.ok(new ApiResponse("success", new Object[] { user }, HttpStatus.OK.value()));
    }
    
    @PostMapping("/login")
    @Operation(summary = "User/Admin Login", description = "Logs in a user using username/email and password")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.loginAndFetchToken(loginRequest);
            return ResponseEntity.ok(new ApiResponse("success", new Object[] { response }, HttpStatus.OK.value()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse(e.getReason(), null, e.getStatusCode().value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


}
