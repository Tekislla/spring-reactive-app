package com.reactive.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.application.dto.UserDepartmentDTO;
import com.reactive.application.model.Department;
import com.reactive.application.service.DepartmentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
@Autowired
private DepartmentService departmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Department> create(@RequestBody Department department){
        return departmentService.createDepartment(department);
    }

    @GetMapping
    public Flux<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{departmentId}")
    public Mono<ResponseEntity<Department>> getDepartmentById(@PathVariable Long departmentId){
        Mono<Department> department = departmentService.findById(departmentId);
        return department.map( u -> ResponseEntity.ok(u))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{departmentId}")
    public Mono<ResponseEntity<Department>> updateDepartmentById(@PathVariable Long departmentId, @RequestBody Department department){
        return departmentService.updateDepartment(departmentId, department)
                .map(updatedDepartment -> ResponseEntity.ok(updatedDepartment))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{departmentId}")
    public Mono<ResponseEntity<Void>> deleteDepartmentById(@PathVariable Long departmentId){
        return departmentService.deleteDepartment(departmentId)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
//    @GetMapping("/depuser/{departmentId}")
//    public UserDepartmentDTO findDepartmentUser(@PathVariable Long departmentId) {
//    	return departmentService.findDepartmentUser(departmentId);
//    }
    
    @GetMapping("/{userId}/department")
    public Mono<UserDepartmentDTO> fetchUserAndDepartment(@PathVariable Long userId){
        return departmentService.fetchUserAndDepartment(userId);
    }

}