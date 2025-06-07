package com.yatranow.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.yatranow.userservice.entity.PasswordReset;
import com.yatranow.userservice.entity.User;
import com.yatranow.userservice.repository.PasswordResetRepository;
import com.yatranow.userservice.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordResetRepository resetRepository;

	private final Random random = new Random();

	/**
	 * Sends an OTP to the specified email address for either password reset or
	 * email verification.
	 *
	 * @param toEmail the email address to send the OTP to
	 * @param type    the type of OTP (either "forgot-password" or
	 *                "email-verification")
	 * @return the generated OTP
	 */
	public String sendOtp(String toEmail, String type) {
		String otp = String.format("%06d", random.nextInt(999999));
		PasswordReset passReset = null;

		try {
			User user = userRepository.findByEmail(toEmail).orElseThrow(() -> new RuntimeException("User not found"));

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(toEmail);
			if (type.equals("forgot-password")) {
				helper.setSubject("Password Reset OTP");
				helper.setText(getPasswordResetEmailTemplate(otp), true);
				passReset = new PasswordReset(user.getId(), otp, LocalDateTime.now().plusMinutes(15));
				resetRepository.save(passReset);
			} else if (type.equals("email-verification")) {
				helper.setSubject("Email Verification OTP");
				helper.setText(getEmailVerificationTemplate(otp), true);
			}

			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return otp;
	}

	/**
	 * Generates an HTML email template for password reset.
	 *
	 * @param otp the OTP to include in the email
	 * @return the HTML email template as a string
	 */
	@SuppressWarnings("preview")
	private String getPasswordResetEmailTemplate(String otp) {
		return String.format("""
				<!DOCTYPE html>
				<html>
				<head>
				    <style>
				        body {
				            font-family: Arial, sans-serif;
				            background-color: #f4f4f4;
				            margin: 0;
				            padding: 0;
				        }
				        .email-container {
				            max-width: 600px;
				            margin: 20px auto;
				            background-color: #ffffff;
				            border: 1px solid #dddddd;
				            border-radius: 8px;
				            overflow: hidden;
				        }
				        .email-header {
				            background-color: #007bff;
				            color: #ffffff;
				            text-align: center;
				            padding: 20px;
				        }
				        .email-body {
				            padding: 20px;
				            color: #333333;
				            line-height: 1.6;
				        }
				        .email-footer {
				            text-align: center;
				            padding: 10px;
				            background-color: #f4f4f4;
				            color: #777777;
				            font-size: 12px;
				        }
				        .btn {
				            display: inline-block;
				            background-color: #007bff;
				            color: #ffffff;
				            text-decoration: none;
				            padding: 10px 20px;
				            border-radius: 5px;
				            margin-top: 20px;
				        }
				        .btn:hover {
				            background-color: #0056b3;
				        }
				    </style>
				</head>
				<body>
				    <div class="email-container">
				        <div class="email-header">
				            <h1>Password Reset OTP</h1>
				        </div>
				        <div class="email-body">
				            <p>Dear User,</p>
				            <p>Please use the following OTP to reset password:</p>
				            <h2 style="text-align: center; color: #007bff;">%s</h2>
				            <p>If you did not request this, please ignore this email.</p>
				            <p>Thank you,<br>YatraNow.com</p>
				        </div>
				        <div class="email-footer">
				            <p>&copy; 2025 YatraNow.com. All rights reserved.</p>
				        </div>
				    </div>
				</body>
				</html>
				""", otp);
	}

	/**
	 * Generates an HTML email template for email verification.
	 *
	 * @param otp the OTP to include in the email
	 * @return the HTML email template as a string
	 */
	@SuppressWarnings("preview")
	private String getEmailVerificationTemplate(String otp) {
		return String.format("""
				<!DOCTYPE html>
				<html>
				<head>
				    <style>
				        body {
				            font-family: Arial, sans-serif;
				            background-color: #f4f4f4;
				            margin: 0;
				            padding: 0;
				        }
				        .email-container {
				            max-width: 600px;
				            margin: 20px auto;
				            background-color: #ffffff;
				            border: 1px solid #dddddd;
				            border-radius: 8px;
				            overflow: hidden;
				        }
				        .email-header {
				            background-color: #007bff;
				            color: #ffffff;
				            text-align: center;
				            padding: 20px;
				        }
				        .email-body {
				            padding: 20px;
				            color: #333333;
				            line-height: 1.6;
				        }
				        .email-footer {
				            text-align: center;
				            padding: 10px;
				            background-color: #f4f4f4;
				            color: #777777;
				            font-size: 12px;
				        }
				        .btn {
				            display: inline-block;
				            background-color: #007bff;
				            color: #ffffff;
				            text-decoration: none;
				            padding: 10px 20px;
				            border-radius: 5px;
				            margin-top: 20px;
				        }
				        .btn:hover {
				            background-color: #0056b3;
				        }
				    </style>
				</head>
				<body>
				    <div class="email-container">
				        <div class="email-header">
				            <h1>Email Verification OTP</h1>
				        </div>
				        <div class="email-body">
				            <p>Dear User,</p>
				            <p>Thank you for signing up. Please use the following OTP to verify your email address:</p>
				            <h2 style="text-align: center; color: #007bff;">%s</h2>
				            <p>If you did not request this, please ignore this email.</p>
				            <p>Thank you,<br>YatraNow.com</p>
				        </div>
				        <div class="email-footer">
				            <p>&copy; 2025 Your Company. All rights reserved.</p>
				        </div>
				    </div>
				</body>
				</html>
				""", otp);
	}

	/**
	 * Sends a simple email.
	 *
	 * @param to      the recipient's email address
	 * @param subject the subject of the email
	 * @param content the content of the email
	 */
	public void sendEmail(String to, String subject, String content) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send email", e);
		}
	}

}
