package com.task.demo.project.service.serviceImpl;

import com.task.demo.project.repository.EmployeeDepartmentRepository;
import com.task.demo.project.repository.EmployeeRepository;
import com.task.demo.project.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDepartmentRepository departmentRepository;

    public DashboardServiceImpl(EmployeeRepository employeeRepository,
                                EmployeeDepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Map<String, Object> employeeTypeDistribution() {
        // Group employees by employeeType (Full-Time, Intern, Contractor, etc.)
        Map<String, Long> counts = employeeRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        e -> (e.getEmployeeType() != null) ? e.getEmployeeType() : "Unknown",
                        Collectors.counting()
                ));

        List<String> labels = new ArrayList<>(counts.keySet());
        List<Long> values = labels.stream().map(counts::get).collect(Collectors.toList());

        return Map.of("labels", labels, "values", values);
    }

    @Override
    public Map<String, Object> departmentDistribution() {
        // Grouping employees by department name
        Map<String, Long> counts = departmentRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        d -> Optional.ofNullable(d.getDepartmentName()).orElse("Unknown"),
                        Collectors.counting()
                ));

        List<String> labels = new ArrayList<>(counts.keySet());
        List<Long> values = labels.stream().map(counts::get).collect(Collectors.toList());

        return Map.of("labels", labels, "values", values);
    }

    @Override
    public Map<String, Object> employeeCountsOverTime() {
        // Counting employees grouped by month-year from createdAt
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, Long> series = employeeRepository.findAll()
                .stream()
                .filter(e -> e.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getCreatedAt().format(fmt),
                        Collectors.counting()
                ));

        List<String> labels = series.keySet().stream().sorted().collect(Collectors.toList());
        List<Long> values = labels.stream().map(series::get).collect(Collectors.toList());

        return Map.of("labels", labels, "values", values);
    }

    @Override
    public Map<String, Object> newEmployeesByMonth() {
        // Use Repository helper query with createdAt
        List<Object[]> rows = employeeRepository.countNewEmployeesByMonth();

        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (Object[] row : rows) {
            labels.add((String) row[0]); // month like "2025-01"
            values.add(((Number) row[1]).longValue());
        }

        return Map.of("labels", labels, "values", values);
    }
}
