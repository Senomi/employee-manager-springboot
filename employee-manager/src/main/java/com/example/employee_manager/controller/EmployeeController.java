
package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Display employee form and list
    @GetMapping
    public String viewHomePage(
            Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String updated,
            @RequestParam(required = false) String deleted) {

        String trimmedKeyword = keyword == null ? "" : keyword.trim();

        model.addAttribute(
                "listEmployees",
                employeeService.getAllEmployees(trimmedKeyword)
        );

        model.addAttribute("employee", new Employee());
        model.addAttribute("keyword", trimmedKeyword);

        if ("added".equals(success)) {
            model.addAttribute(
                    "successMessage",
                    "Employee added successfully!"
            );
        } else if ("yes".equals(updated)) {
            model.addAttribute(
                    "successMessage",
                    "Employee updated successfully!"
            );
        } else if ("yes".equals(deleted)) {
            model.addAttribute(
                    "successMessage",
                    "Employee deleted successfully!"
            );
        }

        return "employees";
    }

    // Save a new employee
    @PostMapping
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model) {

        // Check duplicate email before saving
        if (employee.getEmail() != null
                && !employee.getEmail().trim().isEmpty()
                && employeeService.emailExists(employee.getEmail())) {

            result.rejectValue(
                    "email",
                    "duplicate",
                    "This email is already registered."
            );
        }

        if (result.hasErrors()) {
            model.addAttribute(
                    "listEmployees",
                    employeeService.getAllEmployees("")
            );
            model.addAttribute("keyword", "");
            return "employees";
        }

        employeeService.saveEmployee(employee);

        return "redirect:/employees?success=added";
    }

    // Display edit form
    @GetMapping("/{id}/edit")
    public String showUpdateForm(
            @PathVariable("id") Long id,
            Model model) {

        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);

        return "editemployee";
    }

    // Update an existing employee
    @PostMapping("/{id}/update")
    public String updateEmployee(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model) {

        Employee existingEmployee = employeeService.getEmployeeById(id);

        // Allow the current employee to retain their own email,
        // but reject an email belonging to another employee.
        if (employee.getEmail() != null
                && !employee.getEmail().trim().isEmpty()
                && !employee.getEmail().trim()
                        .equalsIgnoreCase(existingEmployee.getEmail())
                && employeeService.emailExists(employee.getEmail())) {

            result.rejectValue(
                    "email",
                    "duplicate",
                    "This email is already registered."
            );
        }

        if (result.hasErrors()) {
            employee.setId(id);
            model.addAttribute("employee", employee);
            return "editemployee";
        }

        employee.setId(id);
        employeeService.saveEmployee(employee);

        return "redirect:/employees?updated=yes";
    }

    // Delete an employee
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);

        return "redirect:/employees?deleted=yes";
    }

    // Redirect root URL to employee page
    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }
}
