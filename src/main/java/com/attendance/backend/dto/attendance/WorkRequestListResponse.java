package com.attendance.backend.dto.attendance;

import java.util.List;

public record WorkRequestListResponse(
    boolean approvalRequired,
    List<WorkRequestResponse> requests
) {
}
