package com.attendance.backend.dto.attendance;

public class CreateWorkRequestRequest {

    private String requestType;
    private String requestDate;
    private String halfDayType;
    private String occasionType;
    private Integer earlyLeaveMinutes;
    private String reason;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getHalfDayType() {
        return halfDayType;
    }

    public void setHalfDayType(String halfDayType) {
        this.halfDayType = halfDayType;
    }

    public String getOccasionType() {
        return occasionType;
    }

    public void setOccasionType(String occasionType) {
        this.occasionType = occasionType;
    }

    public Integer getEarlyLeaveMinutes() {
        return earlyLeaveMinutes;
    }

    public void setEarlyLeaveMinutes(Integer earlyLeaveMinutes) {
        this.earlyLeaveMinutes = earlyLeaveMinutes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
