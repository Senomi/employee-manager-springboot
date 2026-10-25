package com.example.employee_manager.repository;

import com.example.employee_manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Task 11 Challenge: Search employees by name ignoring case
    List<Employee> findByFullNameContainingIgnoreCase(String keyword);
}