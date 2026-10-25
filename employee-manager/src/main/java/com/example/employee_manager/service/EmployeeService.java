package com.example.employee_manager.service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees(String keyword) {
        String trimmedKeyword = keyword == null ? "" : keyword.trim();
        if (!trimmedKeyword.isEmpty()) {
            return employeeRepository.findByFullNameContainingIgnoreCase(trimmedKeyword);
        }
        return employeeRepository.findAll();
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Employee not found for id :: " + id));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}