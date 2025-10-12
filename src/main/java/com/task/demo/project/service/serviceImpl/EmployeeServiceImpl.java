package com.task.demo.project.service.serviceImpl;

import com.task.demo.project.dto.request.EmployeeDTO;
import com.task.demo.project.dto.response.EmployeeAllResponseDTO;
import com.task.demo.project.entity.EmployeeDepartmentEntity;
import com.task.demo.project.entity.EmployeeEntity;
import com.task.demo.project.repository.EmployeeDepartmentRepository;
import com.task.demo.project.repository.EmployeeRepository;
import com.task.demo.project.service.EmployeeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeDepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }



    // ----------------- MAPPERS -----------------

    private EmployeeDTO convertToDTO(EmployeeEntity employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setPhone(employee.getPhone());
        dto.setSalary(employee.getSalary());
        dto.setAddress(employee.getAddress());

        if (employee.getImages() != null) {
            // kept as absolute URL to preserve current frontend behavior
            dto.setImageUrl("http://localhost:8080/api/employee/" + employee.getId() + "/image");
        }

        return dto;
    }

    public EmployeeAllResponseDTO convertToDto(EmployeeEntity employee) {
        EmployeeAllResponseDTO dto = new EmployeeAllResponseDTO();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setPhone(employee.getPhone());
        dto.setSalary(employee.getSalary());
        dto.setAddress(employee.getAddress());

        // 🔹 Map image URL
        if (employee.getImages() != null) {
            dto.setImageUrl("http://localhost:8080/api/employee/" + employee.getId() + "/image");
        }

        // 🔹 Map department fields
        if (employee.getEmployeeDepartment() != null) {
            dto.setRole(employee.getEmployeeDepartment().getRole());
            dto.setDepartmentName(employee.getEmployeeDepartment().getDepartmentName());
            dto.setLocation(employee.getEmployeeDepartment().getLocation());
            dto.setManagerName(employee.getEmployeeDepartment().getManagerName());
        }

        return dto;
    }


    private EmployeeAllResponseDTO convertToAllResponse(EmployeeEntity employee, EmployeeDepartmentEntity dept) {
        EmployeeAllResponseDTO dto = new EmployeeAllResponseDTO();

        // employee fields
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setPhone(employee.getPhone());
        dto.setSalary(employee.getSalary());
        dto.setAddress(employee.getAddress());

        if (employee.getImages() != null) {
            dto.setImageUrl("http://localhost:8080/api/employee/" + employee.getId() + "/image");
        }else {
            dto.setImageUrl(null);
        }

        // department fields (null-safe)
        if (dept != null) {
            dto.setRole(dept.getRole());
            dto.setDepartmentName(dept.getDepartmentName());
            dto.setLocation(dept.getLocation());
            dto.setManagerName(dept.getManagerName());
        }

        return dto;
    }

    /**
     * Helper to find department by employee id. Many apps model EmployeeDepartment with a FK to Employee.
     * If your repository exposes `Optional<EmployeeDepartmentEntity> findByEmployeeId(Long employeeId)`
     * you can replace the fallback below with that method for correctness.
     */
    private EmployeeDepartmentEntity findDepartmentForEmployee(Long employeeId) {
        // Preferred: uncomment if you have this method declared in EmployeeDepartmentRepository:
        // return departmentRepository.findByEmployeeId(employeeId).orElse(null);

        // Fallback: try to find by the same id (this is what your previous code did). Keep if your dept PK equals employee id.
        return departmentRepository.findById(employeeId).orElse(null);
    }

    // ----------------- SERVICE METHODS -----------------

    @Override
    public List<EmployeeDTO> getAllEmployeeDTOs() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeEntity saveEmployee(EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public EmployeeEntity getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public EmployeeEntity updateEmployee(Long id, EmployeeEntity employee) {
        Optional<EmployeeEntity> opt = employeeRepository.findById(id);
        if (opt.isEmpty()) return null;

        EmployeeEntity existing = opt.get();

        // update only non-null values to avoid unintentionally erasing fields
        if (employee.getName() != null) existing.setName(employee.getName());
        if (employee.getPhone() != null) existing.setPhone(employee.getPhone());
        if (employee.getAddress() != null) existing.setAddress(employee.getAddress());
        if (employee.getSalary() != null) existing.setSalary(employee.getSalary());
        if (employee.getImages() != null) existing.setImages(employee.getImages());
        if (employee.getEmployeeType() != null) existing.setEmployeeType(employee.getEmployeeType());

        return employeeRepository.save(existing);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<EmployeeEntity> getEmployeeByIdOptional(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<EmployeeAllResponseDTO> getAllEmployeeResponses() {
        return employeeRepository.findAll()
                .stream()
                .map(emp -> {
                    EmployeeDepartmentEntity dept = findDepartmentForEmployee(emp.getId());
                    return convertToAllResponse(emp, dept);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeAllResponseDTO> searchEmployees(String type, String keyword) {
        if (type == null || keyword == null) return List.of();

        String lowerKeyword = keyword.toLowerCase();

        List<EmployeeEntity> filtered;
        switch (type.toLowerCase()) {
            case "name":
                filtered = employeeRepository.findAll()
                        .stream()
                        .filter(emp -> emp.getName() != null && emp.getName().toLowerCase().contains(lowerKeyword))
                        .collect(Collectors.toList());
                break;

            case "phone":
                filtered = employeeRepository.findAll()
                        .stream()
                        .filter(emp -> emp.getPhone() != null && emp.getPhone().toLowerCase().contains(lowerKeyword))
                        .collect(Collectors.toList());
                break;

            default:
                filtered = List.of();
        }

        return filtered.stream()
                .map(emp -> {
                    EmployeeDepartmentEntity dept = findDepartmentForEmployee(emp.getId());
                    return convertToAllResponse(emp, dept);
                })
                .collect(Collectors.toList());
    }
}
