package com.reactive.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reactive.application.model.User;
import com.reactive.application.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Mono<User> createUser(User user) {
		return userRepository.save(user);
	}

	public Flux<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Mono<User> findById(Long userId) {
		return userRepository.findById(userId);
	}

	public Mono<User> updateUser(Long userId, User user) {
		return userRepository.findById(userId)
				.flatMap(dbUser -> {
					dbUser.setName(user.getName());
					dbUser.setAge(user.getAge());
					dbUser.setSalary(user.getSalary());
					return userRepository.save(dbUser);
				});
	}

	public Mono<User> deleteUser(Long userId) {
		return userRepository.findById(userId)
				.flatMap(existingUser -> userRepository.delete(existingUser)
						.then(Mono.just(existingUser)));
	}

}