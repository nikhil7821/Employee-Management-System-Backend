package com.task.demo.project.service.serviceImpl;

import com.task.demo.project.dto.request.AdminLoginRequest;
import com.task.demo.project.dto.response.AdminResponse;
import com.task.demo.project.dto.request.AdminSignupRequest;
import com.task.demo.project.entity.AdminEntity;
import com.task.demo.project.repository.AdminRepository;
import com.task.demo.project.service.AdminService;
import com.task.demo.project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<?> signup(AdminSignupRequest request) {
        if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        AdminEntity admin = new AdminEntity();
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setMobile(request.getMobile());
        admin.setAddress(request.getAddress());
        admin.setState(request.getState());
        admin.setCity(request.getCity());

        adminRepository.save(admin);

//        return new AdminResponse(
//                admin.getId(),
//                admin.getFirstName(),
//                admin.getLastName(),
//                admin.getEmail(),
//                admin.getMobile(),
//                admin.getAddress(),
//                admin.getState(),
//                admin.getCity()
//        );
        return new ResponseEntity<>("Admin submitted", HttpStatus.CREATED)  ;
    }

    @Override
    public Map<String, Object> login(AdminLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(request.getEmail());

        Optional<AdminEntity> adminOpt = adminRepository.findByEmail(request.getEmail());
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found");
        }
        AdminEntity admin = adminOpt.get();

        AdminResponse adminResponse = new AdminResponse(
                admin.getId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                admin.getMobile(),
                admin.getAddress(),
                admin.getState(),
                admin.getCity()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("admin", adminResponse);
        response.put("token", token);

        return response;
    }
}
