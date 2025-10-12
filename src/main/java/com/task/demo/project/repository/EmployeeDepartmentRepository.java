package com.task.demo.project.repository;

import com.task.demo.project.entity.EmployeeDepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDepartmentRepository extends JpaRepository<EmployeeDepartmentEntity, Long> {
}
