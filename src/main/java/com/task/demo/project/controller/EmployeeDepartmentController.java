package com.task.demo.project.controller;

import com.task.demo.project.entity.EmployeeDepartmentEntity;
import com.task.demo.project.entity.EmployeeEntity;
import com.task.demo.project.repository.EmployeeDepartmentRepository;
import com.task.demo.project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee-department")
public class EmployeeDepartmentController {

    @Autowired
    private EmployeeDepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // ✅ Get all departments
    @GetMapping("/get-all-department")
    public List<EmployeeDepartmentEntity> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // ✅ Get department by ID
    @GetMapping("/get-department-by-id/{id}")
    public ResponseEntity<EmployeeDepartmentEntity> getDepartmentById(@PathVariable Long id) {
        Optional<EmployeeDepartmentEntity> department = departmentRepository.findById(id);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create department linked to an existing employee
    @PostMapping("/create-department/{employeeId}")
    public ResponseEntity<EmployeeDepartmentEntity> createDepartment(
            @PathVariable Long employeeId,
            @RequestBody EmployeeDepartmentEntity department) {

        Optional<EmployeeEntity> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Link department to employee
        department.setEmployee(employeeOpt.get());
        EmployeeDepartmentEntity savedDepartment = departmentRepository.save(department);
        return ResponseEntity.ok(savedDepartment);
    }

    // ✅ Update department
    @PutMapping("/update-department/{id}")
    public ResponseEntity<EmployeeDepartmentEntity> updateDepartment(
            @PathVariable Long id,
            @RequestBody EmployeeDepartmentEntity updateDepartment) {

        return departmentRepository.findById(id)
                .map(department -> {
                    department.setRole(updateDepartment.getRole());
                    department.setDepartmentName(updateDepartment.getDepartmentName());
                    department.setLocation(updateDepartment.getLocation());
                    department.setManagerName(updateDepartment.getManagerName());
                    EmployeeDepartmentEntity updatedDepartment = departmentRepository.save(department);
                    return ResponseEntity.ok(updatedDepartment);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete department
    @DeleteMapping("/delete-department/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(department -> {
                    departmentRepository.delete(department);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Test endpoint to check controller
    @GetMapping("/test")
    public String testEndpoint() {
        return "EmployeeDepartmentController is working!";
    }
}
