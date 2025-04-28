package com.yatranow.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yatranow.userservice.entity.PasswordReset;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByOtp(String otp);

    @Query("SELECT pr FROM PasswordReset pr WHERE pr.userId = :userId AND pr.otp = :otp")
	Optional<PasswordReset> findByUserIdAndOtp(@Param("userId") Long userId, @Param("otp") String otp);
}