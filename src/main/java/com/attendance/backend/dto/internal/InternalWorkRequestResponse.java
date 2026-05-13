package com.attendance.backend.dto.internal;

public record InternalWorkRequestResponse(
    Long id,
    Long employeeId,
    String employeeCode,
    String employeeName,
    String workplaceName,
    String requestType,
    String requestTypeLabel,
    String status,
    String statusLabel,
    String requestDate,
    String halfDayType,
    String halfDayTypeLabel,
    Integer earlyLeaveMinutes,
    String reason,
    String reviewedByEmployeeCode,
    String reviewedByName,
    String reviewedAt,
    String reviewNote,
    String createdAt
) {
}
