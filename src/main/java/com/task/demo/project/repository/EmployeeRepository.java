package com.task.demo.project.repository;

import com.task.demo.project.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByNameContainingIgnoreCase(String name);
    List<EmployeeEntity> findByPhoneContaining(String phone);

    // For Group by month-year
    @Query("SELECT FUNCTION('DATE_FORMAT', e.createdAt, '%Y-%m') as month, COUNT(e) " +
            "FROM EmployeeEntity e " +
            "GROUP BY FUNCTION('DATE_FORMAT', e.createdAt, '%Y-%m') " +
            "ORDER BY month")
    List<Object[]> countNewEmployeesByMonth();

    // For Counting employees over time
    @Query("SELECT FUNCTION('DATE_FORMAT', e.createdAt, '%Y-%m') as month, COUNT(e) " +
            "FROM EmployeeEntity e " +
            "GROUP BY FUNCTION('DATE_FORMAT', e.createdAt, '%Y-%m') " +
            "ORDER BY month")
    List<Object[]> employeeCountsOverTime();




}
