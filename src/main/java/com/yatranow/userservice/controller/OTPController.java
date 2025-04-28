package com.yatranow.userservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yatranow.userservice.response.ApiResponse;
import com.yatranow.userservice.service.EmailService;
import com.yatranow.userservice.service.OtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users/otp")
public class OTPController {
	
	private static final String OTPTYPE = "email-verification";

	@Autowired
	private EmailService emailService;

    private final Map<String, String> otpStorage = new HashMap<>();
    
    @Autowired
    private OtpService otpService;

    /**
	 * Generates an OTP for the specified mobile number and sends it via SMS.
	 *
	 * @param mobileNumber The mobile number to which the OTP will be sent.
	 * @return A message indicating that the OTP has been sent.
	 */
    @GetMapping("/mobile/generate")
    @Operation(summary = "Generate mobile OTP", description = "Sends an OTP to the given mobile number.")
    public ResponseEntity<ApiResponse> generateOtp(
    		@Parameter(description = "The Mobile number to which the OTP will be sent.", required = true)
    		@RequestParam("mobileNumber") String mobileNumber) {
        String otp = otpService.generateOtp(mobileNumber);
        System.out.println("Generated OTP: " + otp);
        return ResponseEntity.ok(new ApiResponse("OTP sent to " + mobileNumber, null, 200));
    }
    
    /**
	 * Validates the OTP for the specified mobile number.
	 *
	 * @param mobileNumber The mobile number for which the OTP is being validated.
	 * @param otp          The OTP to be validated.
	 * @return ResponseEntity with a success message if the OTP is valid,
	 *         or an error message if the OTP is invalid.
	 */
    @PostMapping("/mobile/validate")
    @Operation(summary = "Validate mobile OTP", description = "Validates the OTP for the given mobile number.")
    public ResponseEntity<ApiResponse> validateOtp(
            @Parameter(description = "The mobile number for which the OTP is being validated.", required = true)
            @RequestParam("mobileNumber") String mobileNumber,
            @Parameter(description = "The OTP to be validated.", required = true)
            @RequestParam("otp") String otp) {
        boolean isValid = otpService.validateOtp(mobileNumber, otp);
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse("success", null, 200));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Invalid OTP", null, 400));
    }


    /**
     * Sends an OTP to the specified email address.
     *
     * @param email The email address to which the OTP will be sent.
     * @return ResponseEntity with a message indicating that the OTP has been sent.
     */
    @PostMapping("/email/send")
    @Operation(summary = "Send OTP", description = "Sends an OTP to the specified email address.")
    public ResponseEntity<ApiResponse> sendOtp(
            @Parameter(description = "The email address to which the OTP will be sent.", required = true)
            @RequestBody String email) {
        // Simulate sending OTP
        String otp = emailService.sendOtp(email, OTPTYPE);
        otpStorage.put(email, otp);
        return ResponseEntity.ok(new ApiResponse("OTP sent to " + email, null, 200));
    }

    /**
     * Verifies the OTP for the specified email address.
     *
     * @param email The email address for which the OTP is being verified.
     * @param otp   The OTP to be verified.
     * @return ResponseEntity with a success message if the OTP is valid,
     *         or an error message if the OTP is invalid.
     */
    @PostMapping("/email/verify")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP for the specified email address.")
    public ResponseEntity<ApiResponse> verifyOtp(
            @Parameter(description = "The email address for which the OTP is being verified.", required = true)
            @RequestParam("email") String email,
            @Parameter(description = "The OTP to be verified.", required = true)
            @RequestParam("otp") String otp) {
        String storedOtp = otpStorage.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email);
            return ResponseEntity.ok(new ApiResponse("success", null, 200));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Invalid OTP", null, 400));
    }
}
