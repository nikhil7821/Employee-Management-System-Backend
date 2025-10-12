package com.task.demo.project.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "employee_department")
public class EmployeeDepartmentEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private EmployeeEntity employee;

    @Column(nullable = false)
    private String role;

    @Column(name = "department_name")
    private String departmentName;

    @Column
    private String location;

    @Column(name = "manager_name")
    private String managerName;

    //Default Constructor by Hibernste
    public EmployeeDepartmentEntity(){

    }
    //Constructors-generated
    public EmployeeDepartmentEntity(String role, String departmentName, String location, String managerName) {
        this.role = role;
        this.departmentName = departmentName;
        this.location = location;
        this.managerName = managerName;
    }

    //Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
