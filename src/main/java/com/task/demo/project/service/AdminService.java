
        package com.task.demo.project.service;

import com.task.demo.project.dto.request.AdminLoginRequest;
import com.task.demo.project.dto.request.AdminSignupRequest;
import com.task.demo.project.dto.response.AdminResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AdminService {

    ResponseEntity<?> signup(AdminSignupRequest request);
    Map<String, Object> login(AdminLoginRequest request);
}
