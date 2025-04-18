package com.yatranow.userservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String otp;

    private LocalDateTime expirationTime;

    private boolean isUsed;

    public PasswordReset() {}

    public PasswordReset(Long userId, String otp, LocalDateTime expirationTime) {
        this.userId = userId;
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.isUsed = false;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
}

