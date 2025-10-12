package com.task.demo.project.service;

import java.util.Map;
import java.util.function.ObjLongConsumer;

public interface DashboardService {

    Map<String, Object> employeeTypeDistribution();
    Map<String, Object> departmentDistribution();
    Map<String, Object> employeeCountsOverTime();
    Map<String, Object> newEmployeesByMonth();
}
