package com.task.demo.project.controller;

import com.task.demo.project.service.DashboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    // ✅ Pie Chart (Employee Type Distribution)
    @GetMapping("/employee-type-distribution")
    public Map<String, Object> getEmployeeTypeDistribution(){
        return dashboardService.employeeTypeDistribution();
    }

    // ✅ Doughnut Chart (Department Distribution)
    @GetMapping("/department-distribution")
    public Map<String, Object> getDepartmentDistribution(){
        return dashboardService.departmentDistribution();
    }

    // ✅ Line Chart (Employee Counts Over Time)
    @GetMapping("/employee-counts-over-time")
    public Map<String, Object> getEmployeeCountsOverTime(){
        return dashboardService.employeeCountsOverTime();
    }

    // ✅ Bar Chart (New Employees by Month)
    @GetMapping("/new-employees-by-month")
    public Map<String, Object> getNewEmployeesByMonth(){
        return dashboardService.newEmployeesByMonth();
    }

    // ✅ Test Endpoint
    @GetMapping("/test")
    public String testDashboard() {
        return "DashboardController is working!";
    }
}
