package com.reactive.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDepartmentDTO {
    private Long userId;
    private String userName;
    private int age;
    private double salary;
    private Long departmentId;
    private String departmentName;
    private String loc;
}