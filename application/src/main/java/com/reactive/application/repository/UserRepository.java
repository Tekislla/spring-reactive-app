package com.reactive.application.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.reactive.application.model.User;

public interface UserRepository extends ReactiveCrudRepository<User,Long> {
 
}