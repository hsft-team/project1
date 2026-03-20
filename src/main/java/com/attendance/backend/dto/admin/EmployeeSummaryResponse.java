package com.attendance.backend.dto.admin;

public class EmployeeSummaryResponse {

    private final Long employeeId;
    private final String employeeCode;
    private final String employeeName;
    private final String role;
    private final String companyName;

    public EmployeeSummaryResponse(
        Long employeeId,
        String employeeCode,
        String employeeName,
        String role,
        String companyName
    ) {
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.role = role;
        this.companyName = companyName;
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

    public String getRole() {
        return role;
    }

    public String getCompanyName() {
        return companyName;
    }
}
