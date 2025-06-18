package com.yatranow.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yatranow.userservice.entity.UserQuery;

public interface UserQueryRepository extends JpaRepository<UserQuery, Long> {
}
