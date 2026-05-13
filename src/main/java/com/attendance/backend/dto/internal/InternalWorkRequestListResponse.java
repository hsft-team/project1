package com.attendance.backend.dto.internal;

import java.util.List;

public record InternalWorkRequestListResponse(
    boolean approvalRequired,
    boolean workplaceScopedAdmin,
    List<InternalWorkRequestResponse> requests
) {
}
