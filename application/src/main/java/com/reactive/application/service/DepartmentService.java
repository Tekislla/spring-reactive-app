package com.reactive.application.service;

import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reactive.application.dto.UserDepartmentDTO;
import com.reactive.application.model.Department;
import com.reactive.application.model.User;
import com.reactive.application.repository.DepartmentRepository;
import com.reactive.application.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Transactional
public class DepartmentService {
	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private UserRepository userRepository;

	public Mono<Department> createDepartment(Department department) {
		return departmentRepository.save(department);
	}

	public Flux<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}

	public Mono<Department> findById(Long departmentId) {
		return departmentRepository.findById(departmentId);
	}

	public Mono<Department> updateDepartment(Long departmentId, Department department) {
		return departmentRepository.findById(departmentId)
				.flatMap(dbDepartment -> {
					dbDepartment.setName(department.getName());
					dbDepartment.setLoc(department.getLoc());
					dbDepartment.setUserId(department.getUserId());
					return departmentRepository.save(dbDepartment);
				});
	}

	public Mono<Department> deleteDepartment(Long departmentId) {
		return departmentRepository.findById(departmentId)
				.flatMap(existingDepartment -> departmentRepository.delete(existingDepartment)
						.then(Mono.just(existingDepartment)));
	}

//	public UserDepartmentDTO findDepartmentUser(Long departmentId) {
//		Mono<Department> bdDepartment = departmentRepository.findById(departmentId);
//		Department department = bdDepartment.block();
//
//		Mono<User> bdUser = userRepository.findById(department.getId());
//		User user = bdUser.block();
//
//		UserDepartmentDTO userDepartment = new UserDepartmentDTO();
//
//		userDepartment.setDepartmentId(department.getId());
//		userDepartment.setDepartmentName(department.getName());
//		userDepartment.setLoc(department.getLoc());
//		userDepartment.setUserId(department.getUserId());
//		userDepartment.setUserName(user.getName());
//		userDepartment.setAge(user.getAge());
//		userDepartment.setSalary(user.getSalary());
//
//		return userDepartment;
//	}
	
    private Mono<Department> getDepartmentByUserId(Long userId){
        return departmentRepository.findByUserId(userId);
    }

    public Mono<UserDepartmentDTO> fetchUserAndDepartment(Long userId){
        Mono<User> user = userRepository.findById(userId).subscribeOn(Schedulers.boundedElastic());
        Mono<Department> department = getDepartmentByUserId(userId).subscribeOn(Schedulers.boundedElastic());
        return Mono.zip(user, department, userDepartmentDTOBiFunction);
    }

    private BiFunction<User, Department, UserDepartmentDTO> userDepartmentDTOBiFunction = (user, department) -> UserDepartmentDTO.builder()
            .age(user.getAge())
            .departmentId(department.getId())
            .departmentName(department.getName())
            .userName(user.getName())
            .userId(user.getId())
            .loc(department.getLoc())
            .salary(user.getSalary()).build();


}