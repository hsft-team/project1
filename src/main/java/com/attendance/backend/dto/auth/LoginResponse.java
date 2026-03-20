package com.attendance.backend.dto.auth;

public class LoginResponse {

    private final String accessToken;
    private final String tokenType;
    private final Long employeeId;
    private final String employeeCode;
    private final String employeeName;
    private final String companyName;
    private final String role;

    public LoginResponse(
        String accessToken,
        String tokenType,
        Long employeeId,
        String employeeCode,
        String employeeName,
        String companyName,
        String role
    ) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.companyName = companyName;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRole() {
        return role;
    }
}
