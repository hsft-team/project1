package com.attendance.backend.dto.auth;

public class ChangePasswordResponse {

    private final String message;

    public ChangePasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
