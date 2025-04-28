package com.yatranow.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yatranow.userservice.request.PasswordResetRequest;
import com.yatranow.userservice.response.ApiResponse;
import com.yatranow.userservice.service.PasswordResetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    @Operation(
        summary = "Forgot Password",
        description = "Sends a password reset OTP to the provided email address."
    )
    public ResponseEntity<ApiResponse> forgotPassword(
        @Parameter(description = "The email address to send the password reset OTP.", required = true)
        @RequestBody String email) {
        try {
            passwordResetService.sendResetOtp(email);
            return ResponseEntity.ok(new ApiResponse("success", null, 200));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null, 400));
        }
    }

    @PostMapping("/reset-password")
    @Operation(
        summary = "Reset Password",
        description = "Resets the password using the provided token and new password."
    )
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.resetPassword(request.getOtp(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse("Password reset successfully", null, 200));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null, 400));
        }
    }
}


