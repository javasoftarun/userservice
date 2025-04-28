package com.yatranow.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yatranow.userservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);

	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.phone = :mobile")
	Optional<User> findByPhone(@Param("mobile") String mobile);

	@Query("SELECT u FROM User u WHERE u.email = :email and u.password = :password")
	Optional<User> findByUsernameAndPassword(@Param("email") String email, @Param("password") String password);
}
