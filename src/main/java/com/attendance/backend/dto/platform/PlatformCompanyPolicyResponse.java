package com.attendance.backend.dto.platform;

import java.time.LocalDateTime;
import java.util.List;

public record PlatformCompanyPolicyResponse(
    Long backendCompanyId,
    String companyName,
    long currentEmployeeCount,
    boolean featureAttendanceEnabled,
    boolean featureStatisticsEnabled,
    boolean featureEmployeeManagementEnabled,
    boolean adsenseEnabled,
    boolean paidEnabled,
    String subscriptionStatus,
    Integer maxEmployeeCount,
    Integer statisticsRetentionDays,
    boolean allowExcelExport,
    String adminNote,
    List<String> enabledFeatures,
    LocalDateTime lastSyncedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
