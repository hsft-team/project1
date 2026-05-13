package com.attendance.backend.dto.internal;

import java.util.List;

public record InternalWorkRequestUploadResponse(
    int successCount,
    int failureCount,
    List<String> failureMessages
) {
}
