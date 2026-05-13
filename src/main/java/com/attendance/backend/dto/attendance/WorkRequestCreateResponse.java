package com.attendance.backend.dto.attendance;

public record WorkRequestCreateResponse(
    WorkRequestResponse request,
    String message
) {
}
