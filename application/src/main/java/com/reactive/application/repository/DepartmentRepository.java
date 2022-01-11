package com.reactive.application.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.reactive.application.model.Department;

import reactor.core.publisher.Mono;

public interface DepartmentRepository extends ReactiveCrudRepository<Department,Long> {
    Mono<Department> findByUserId(Long userId);
    
}