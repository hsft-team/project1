package com.attendance.backend.service;

import com.attendance.backend.domain.entity.Employee;
import com.attendance.backend.domain.repository.EmployeeRepository;
import com.attendance.backend.dto.auth.LoginRequest;
import com.attendance.backend.dto.auth.LoginResponse;
import com.attendance.backend.exception.UnauthorizedException;
import com.attendance.backend.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
        EmployeeRepository employeeRepository,
        PasswordEncoder passwordEncoder,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        Employee employee = employeeRepository.findByEmployeeCode(request.getEmployeeCode())
            .orElseThrow(() -> new UnauthorizedException("사번 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new UnauthorizedException("사번 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.generateToken(employee.getId(), employee.getEmployeeCode());

        return new LoginResponse(
            token,
            "Bearer",
            employee.getId(),
            employee.getEmployeeCode(),
            employee.getName(),
            employee.getCompany().getName(),
            employee.getRole().name()
        );
    }
}
