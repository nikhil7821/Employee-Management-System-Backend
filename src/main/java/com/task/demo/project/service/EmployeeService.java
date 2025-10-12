package com.task.demo.project.service;

import com.task.demo.project.dto.request.EmployeeDTO;
import com.task.demo.project.dto.response.EmployeeAllResponseDTO;
import com.task.demo.project.entity.EmployeeEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeEntity saveEmployee(EmployeeEntity employee);
    List<EmployeeEntity> getAllEmployees();
    EmployeeEntity getEmployeeById(Long id);
    EmployeeEntity updateEmployee(Long id, EmployeeEntity employee);
    void deleteEmployee(Long id);
    Optional<EmployeeEntity> getEmployeeByIdOptional(Long id);

    List<EmployeeDTO> getAllEmployeeDTOs();
    List<EmployeeAllResponseDTO> getAllEmployeeResponses();
    List<EmployeeAllResponseDTO> searchEmployees(String type, String keyword);
    EmployeeAllResponseDTO convertToDto(EmployeeEntity employee);


}
