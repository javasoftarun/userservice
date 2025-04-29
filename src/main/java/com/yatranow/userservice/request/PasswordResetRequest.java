package com.yatranow.userservice.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordResetRequest {

	@NotNull
	@Size(min = 6, max = 6, message = "OTP must be 6 characters")
	@Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits")
	private String otp;
	
	private String newPassword;
	
	@NotNull
	private String username;
	
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
