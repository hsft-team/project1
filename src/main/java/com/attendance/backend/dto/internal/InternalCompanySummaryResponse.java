package com.attendance.backend.dto.internal;

import java.time.LocalDateTime;

public record InternalCompanySummaryResponse(
    Long companyId,
    String companyName,
    String plan,
    Integer employeeLimit,
    Integer workplaceLimit,
    Double latitude,
    Double longitude,
    Integer allowedRadiusMeters,
    long employeeCount,
    long activeEmployeeCount,
    long workplaceCount,
    String adminEmployeeCode,
    String adminName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
