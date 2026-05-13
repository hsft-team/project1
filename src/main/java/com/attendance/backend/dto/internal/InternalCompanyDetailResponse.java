package com.attendance.backend.dto.internal;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record InternalCompanyDetailResponse(
    Long companyId,
    String companyName,
    String plan,
    Integer employeeLimit,
    Integer workplaceLimit,
    Double latitude,
    Double longitude,
    Integer allowedRadiusMeters,
    LocalTime lateAfterTime,
    String noticeMessage,
    String mobileSkinKey,
    boolean enforceSingleDeviceLogin,
    long employeeCount,
    long activeEmployeeCount,
    long workplaceCount,
    String adminEmployeeCode,
    String adminName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
