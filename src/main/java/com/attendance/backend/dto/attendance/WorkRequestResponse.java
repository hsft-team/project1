package com.attendance.backend.dto.attendance;

public record WorkRequestResponse(
    Long id,
    String requestType,
    String requestTypeLabel,
    String status,
    String statusLabel,
    String requestDate,
    String halfDayType,
    String halfDayTypeLabel,
    String occasionType,
    String occasionTypeLabel,
    Integer earlyLeaveMinutes,
    String reason,
    boolean cancelable,
    String reviewedByEmployeeCode,
    String reviewedByName,
    String reviewedAt,
    String reviewNote,
    String createdAt
) {
}
