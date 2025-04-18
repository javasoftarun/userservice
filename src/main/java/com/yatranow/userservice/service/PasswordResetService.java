package com.yatranow.userservice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatranow.userservice.config.CommonUtils;
import com.yatranow.userservice.entity.PasswordReset;
import com.yatranow.userservice.entity.User;
import com.yatranow.userservice.repository.PasswordResetRepository;
import com.yatranow.userservice.repository.UserRepository;

@Service
public class PasswordResetService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordResetRepository resetRepository;

	@Autowired
	private EmailService emailService;

	public static final String OTPTYPE = "forgot-password";

	public void sendResetOtp(String email) throws RuntimeException {

		emailService.sendOtp(email, OTPTYPE);
	}

	/**
	 * Resets the password for a user using the provided OTP and new password.
	 *
	 * @param otp         The OTP received for password reset.
	 * @param newPassword The new password to set.
	 * @throws RuntimeException if the OTP is invalid or expired, or if the user is
	 *                          not found.
	 */
	public void resetPassword(String otp, String newPassword) {

		PasswordReset passReset = resetRepository.findByOtp(otp).orElseThrow(() -> new RuntimeException("Invalid OTP"));

		if (passReset.getExpirationTime().isBefore(LocalDateTime.now()) || passReset.isUsed()) {
			throw new RuntimeException("OTP expired or already used");
		}

		User user = userRepository.findById(passReset.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		user.setPassword(CommonUtils.encryptPassword(newPassword));
		userRepository.save(user);

		passReset.setUsed(true);
		resetRepository.save(passReset);
	}
}
