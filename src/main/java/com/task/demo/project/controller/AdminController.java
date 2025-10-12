package com.task.demo.project.controller;

import com.task.demo.project.dto.request.AdminLoginRequest;
import com.task.demo.project.dto.request.AdminSignupRequest;
import com.task.demo.project.dto.response.AdminResponse;
import com.task.demo.project.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AdminSignupRequest request) {
        logger.info("Admin signup request received for email: {}", request.getEmail());

        try {
            ResponseEntity<?> response = adminService.signup(request);
            logger.info("Admin signup successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Admin signup failed for email: {}", request.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AdminLoginRequest request) {
        logger.info("Admin login request received for email: {}", request.getEmail());

        try {
            Map<String, Object> response = adminService.login(request);
            logger.info("Admin login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Admin login failed for email: {}", request.getEmail(), e);
            throw e;
        }
    }
}