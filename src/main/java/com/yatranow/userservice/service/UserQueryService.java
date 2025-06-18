package com.yatranow.userservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yatranow.userservice.entity.UserQuery;
import com.yatranow.userservice.repository.UserQueryRepository;

@Service
public class UserQueryService {
    @Autowired
    private UserQueryRepository repository;

    public UserQuery save(UserQuery userQuery) {
        return repository.save(userQuery);
    }

	public List<UserQuery> findAll() {
		  return repository.findAll();
	}
}
