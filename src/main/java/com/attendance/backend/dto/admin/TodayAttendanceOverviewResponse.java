package com.attendance.backend.dto.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TodayAttendanceOverviewResponse {

    private final Long employeeId;
    private final String employeeCode;
    private final String employeeName;
    private final LocalDate attendanceDate;
    private final boolean checkedIn;
    private final LocalDateTime checkInTime;
    private final LocalDateTime checkOutTime;
    private final boolean late;
    private final String status;

    public TodayAttendanceOverviewResponse(
        Long employeeId,
        String employeeCode,
        String employeeName,
        LocalDate attendanceDate,
        boolean checkedIn,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        boolean late,
        String status
    ) {
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.attendanceDate = attendanceDate;
        this.checkedIn = checkedIn;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.late = late;
        this.status = status;
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

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public boolean isLate() {
        return late;
    }

    public String getStatus() {
        return status;
    }
}
