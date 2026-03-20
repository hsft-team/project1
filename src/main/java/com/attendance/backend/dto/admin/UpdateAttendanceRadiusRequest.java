package com.attendance.backend.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateAttendanceRadiusRequest {

    @NotNull
    @Min(1)
    private Integer allowedRadiusMeters;

    public Integer getAllowedRadiusMeters() {
        return allowedRadiusMeters;
    }

    public void setAllowedRadiusMeters(Integer allowedRadiusMeters) {
        this.allowedRadiusMeters = allowedRadiusMeters;
    }
}
