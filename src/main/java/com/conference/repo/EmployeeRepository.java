package com.conference.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
	