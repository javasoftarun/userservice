package com.yatranow.userservice.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordResetRequest {

	@NotNull
	@Size(min = 6, max = 6, message = "OTP must be 6 characters")
	@Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits")
	private String otp;
	@NotNull
	@Size(min = 8, max = 25, message = "Password must be between 8 and 25 characters")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
	private String newPassword;
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
	
}
