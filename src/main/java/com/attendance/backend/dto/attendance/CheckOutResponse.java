package com.attendance.backend.dto.attendance;

import com.attendance.backend.domain.entity.AttendanceStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckOutResponse {

    private final Long attendanceRecordId;
    private final LocalDate attendanceDate;
    private final LocalDateTime checkInTime;
    private final LocalDateTime checkOutTime;
    private final AttendanceStatus status;
    private final String message;

    public CheckOutResponse(
        Long attendanceRecordId,
        LocalDate attendanceDate,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        AttendanceStatus status,
        String message
    ) {
        this.attendanceRecordId = attendanceRecordId;
        this.attendanceDate = attendanceDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.message = message;
    }

    public Long getAttendanceRecordId() {
        return attendanceRecordId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
