package com.attendance.backend.dto.attendance;

public record WorkRequestActionResponse(
    WorkRequestResponse request,
    String message
) {
}
