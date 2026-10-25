package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // GET /employees - Show form and table (with optional search keyword)
    @GetMapping
    public String viewHomePage(Model model, @RequestParam(required = false) String keyword) {
        String trimmedKeyword = keyword == null ? "" : keyword.trim();
        model.addAttribute("listEmployees", employeeService.getAllEmployees(trimmedKeyword));
        model.addAttribute("employee", new Employee());
        model.addAttribute("keyword", trimmedKeyword);
        return "employees";
    }

    // POST /employees - Save new employee
    @PostMapping
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listEmployees", employeeService.getAllEmployees(null));
            return "employees";
        }
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // GET /employees/{id}/edit - Show edit form
    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "editemployee";
    }

    // POST /employees/{id}/update - Save changes
    @PostMapping("/{id}/update")
    public String updateEmployee(@PathVariable("id") Long id, @Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            employee.setId(id);
            model.addAttribute("employee", employee);
            return "editemployee";
        }
        employee.setId(id);
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // POST /employees/{id}/delete - Delete employee
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}