package com.attendance.backend.dto.internal;

import java.time.LocalTime;

public record InternalCompanyUpdateRequest(
    String companyName,
    Double latitude,
    Double longitude,
    Integer allowedRadiusMeters,
    LocalTime lateAfterTime,
    String noticeMessage,
    String mobileSkinKey,
    boolean enforceSingleDeviceLogin
) {
}
