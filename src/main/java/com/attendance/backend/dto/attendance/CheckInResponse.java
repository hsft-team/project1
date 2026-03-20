package com.attendance.backend.dto.attendance;

import java.time.LocalDateTime;

public class CheckInResponse {

    private final LocalDateTime checkInTime;
    private final boolean late;
    private final String message;

    public CheckInResponse(LocalDateTime checkInTime, boolean late, String message) {
        this.checkInTime = checkInTime;
        this.late = late;
        this.message = message;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public boolean isLate() {
        return late;
    }

    public String getMessage() {
        return message;
    }
}
