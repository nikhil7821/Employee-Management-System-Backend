package com.task.demo.project.controller;

import com.task.demo.project.dto.request.EmployeeDTO;
import com.task.demo.project.dto.response.EmployeeAllResponseDTO;
import com.task.demo.project.entity.EmployeeDepartmentEntity;
import com.task.demo.project.entity.EmployeeEntity;
import com.task.demo.project.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
        logger.info("EmployeeController initialized successfully");
    }

    @PostMapping("/create-employee")
    public EmployeeEntity createEmployee(@RequestBody EmployeeEntity employee){
        logger.info("Creating new employee with name: {}", employee.getName());
        try {
            EmployeeEntity savedEmployee = employeeService.saveEmployee(employee);
            logger.info("Employee created successfully with ID: {}", savedEmployee.getId());
            return savedEmployee;
        } catch (Exception e) {
            logger.error("Error creating employee: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-all-employee-responses")
    public List<EmployeeAllResponseDTO> getAllEmployeeResponses() {
        logger.info("Fetching all employee responses");
        try {
            List<EmployeeAllResponseDTO> responses = employeeService.getAllEmployeeResponses();
            logger.info("Retrieved {} employee responses", responses.size());
            return responses;
        } catch (Exception e) {
            logger.error("Error fetching employee responses: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-all-employee")
    public List<EmployeeEntity> getAllEmployees(){
        logger.info("Fetching all employees");
        try {
            List<EmployeeEntity> employees = employeeService.getAllEmployees();
            logger.info("Retrieved {} employees", employees.size());
            return employees;
        } catch (Exception e) {
            logger.error("Error fetching all employees: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-all-employee-dto")
    public List<EmployeeDTO> getAllEmployeesDTO() {
        logger.info("Fetching all employees as DTOs");
        try {
            List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployeeDTOs();
            logger.info("Retrieved {} employee DTOs", employeeDTOs.size());
            return employeeDTOs;
        } catch (Exception e) {
            logger.error("Error fetching employee DTOs: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-employee-by-id/{id}")
    public ResponseEntity<EmployeeAllResponseDTO> getEmployeeById(@PathVariable Long id) {
        logger.info("Fetching employee by ID: {}", id);

        try {
            Optional<EmployeeEntity> employeeOpt = employeeService.getEmployeeByIdOptional(id);

            if (employeeOpt.isEmpty()) {
                logger.warn("No employee found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            EmployeeEntity employee = employeeOpt.get();

            EmployeeAllResponseDTO dto = new EmployeeAllResponseDTO();
            dto.setId(employee.getId());
            dto.setName(employee.getName());
            dto.setPhone(employee.getPhone());
            dto.setSalary(employee.getSalary());
            dto.setAddress(employee.getAddress());// Add these lines in your getEmployeeById method after setting other fields:
            dto.setEmployeeType(employee.getEmployeeType());
            dto.setCreatedAt(employee.getCreatedAt());

            // 🔹 Image URL mapping
            if (employee.getImages() != null) {
                dto.setImageUrl("http://localhost:8080/api/employee/" + employee.getId() + "/image");
            }

            // 🔹 Department fields mapping
            if (employee.getEmployeeDepartment() != null) {
                dto.setRole(employee.getEmployeeDepartment().getRole());
                dto.setDepartmentName(employee.getEmployeeDepartment().getDepartmentName());
                dto.setLocation(employee.getEmployeeDepartment().getLocation());
                dto.setManagerName(employee.getEmployeeDepartment().getManagerName());
            }

            logger.info("Employee found with ID: {}", id);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            logger.error("Error fetching employee with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update-employee/{id}")
    public ResponseEntity<EmployeeAllResponseDTO> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeAllResponseDTO dto) {

        EmployeeEntity existing = employeeService.getEmployeeById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // 🔹 Update employee fields
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getPhone() != null) existing.setPhone(dto.getPhone());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
        if (dto.getSalary() != null) existing.setSalary(dto.getSalary());

        // 🔹 Update department fields
        EmployeeDepartmentEntity dept = existing.getEmployeeDepartment();
        if (dept != null) {
            if (dto.getRole() != null) dept.setRole(dto.getRole());
            if (dto.getDepartmentName() != null) dept.setDepartmentName(dto.getDepartmentName());
            if (dto.getLocation() != null) dept.setLocation(dto.getLocation());
            if (dto.getManagerName() != null) dept.setManagerName(dto.getManagerName());
        }

        // 🔹 Save updated entity
        employeeService.saveEmployee(existing);

        // 🔹 Convert back to DTO
        EmployeeAllResponseDTO updatedDto = employeeService.convertToDto(existing);

        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable Long id){
        logger.info("Deleting employee with ID: {}", id);
        try {
            employeeService.deleteEmployee(id);
            logger.info("Employee deleted successfully with ID: {}", id);
            return "Employee deleted successfully...!";
        } catch (Exception e) {
            logger.error("Error deleting employee with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/upload-image/{id}")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        logger.info("Uploading image for employee ID: {}, file size: {} bytes", id, file.getSize());

        return employeeService.getEmployeeByIdOptional(id)
                .map(employee -> {
                    try {
                        employee.setImages(file.getBytes());
                        employeeService.saveEmployee(employee);
                        logger.info("Image uploaded successfully for employee ID: {}", id);
                        return ResponseEntity.ok("Image uploaded successfully!");
                    } catch (IOException e) {
                        logger.error("IO error uploading image for employee ID {}: {}", id, e.getMessage(), e);
                        return ResponseEntity.status(500)
                                .body("Error uploading image: " + e.getMessage());
                    } catch (Exception e) {
                        logger.error("Unexpected error uploading image for employee ID {}: {}", id, e.getMessage(), e);
                        return ResponseEntity.status(500)
                                .body("Error uploading image: " + e.getMessage());
                    }
                })
                .orElseGet(() -> {
                    logger.warn("Employee not found for image upload, ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable Long id){
        logger.info("Retrieving image for employee ID: {}", id);

        try {
            Optional<EmployeeEntity> employeeOpt = employeeService.getEmployeeByIdOptional(id);

            if (employeeOpt.isPresent() && employeeOpt.get().getImages() != null) {
                byte[] imageBytes = employeeOpt.get().getImages();
                logger.info("Image retrieved successfully for employee ID: {}, size: {} bytes", id, imageBytes.length);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            }

            logger.warn("No image found for employee ID: {}", id);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Error retrieving image for employee ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public List<EmployeeAllResponseDTO> searchEmployees(
            @RequestParam String type,
            @RequestParam String keyword) {

        logger.info("Searching employees by type: {} with keyword: {}", type, keyword);

        try {
            List<EmployeeAllResponseDTO> results = employeeService.searchEmployees(type, keyword);
            logger.info("Search completed. Found {} employees matching criteria", results.size());
            return results;
        } catch (Exception e) {
            logger.error("Error searching employees with type '{}' and keyword '{}': {}", type, keyword, e.getMessage(), e);
            throw e;
        }
    }
}