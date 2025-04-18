package com.yatranow.userservice.service;

import org.springframework.stereotype.Service;

import com.yatranow.userservice.beans.OtpEntry;

import java.util.HashMap;
import java.util.Random;

@Service
public class OtpService {

	private final HashMap<String, OtpEntry> otpStorage = new HashMap<>();
	private final Random random = new Random();
	private static final long OTP_VALIDITY_DURATION = 1 * 60 * 1000; // 5 minutes in milliseconds

	public String generateOtp(String mobileNumber) {
		String otp = String.format("%04d", random.nextInt(10000)); // Generate 4-digit OTP
		long expiryTime = System.currentTimeMillis() + OTP_VALIDITY_DURATION;
		otpStorage.put(mobileNumber, new OtpEntry(otp, expiryTime));
		return otp;
	}

	public boolean validateOtp(String mobileNumber, String otp) {
		if (!otpStorage.containsKey(mobileNumber)) {
			return false;
		}

		OtpEntry otpEntry = otpStorage.get(mobileNumber);
		if (System.currentTimeMillis() > otpEntry.getExpiryTime()) {
			otpStorage.remove(mobileNumber); // Remove expired OTP
			return false;
		}

		return otpEntry.getOtp().equals(otp);
	}
}
