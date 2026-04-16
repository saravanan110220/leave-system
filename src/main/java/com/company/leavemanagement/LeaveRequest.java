package com.company.leavemanagement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveRequest {
    private String employeeId;
    private String leaveType; // ANNUAL, SICK, UNPAID
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // PENDING, APPROVED, REJECTED
    private String reason;

    public LeaveRequest(String employeeId, String leaveType, LocalDate startDate, 
                       LocalDate endDate, String reason) {
        
        // Validation
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (leaveType == null || leaveType.isEmpty()) {
            throw new IllegalArgumentException("Leave type cannot be null or empty");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "PENDING";
        this.reason = reason;
    }

    // Getters
    public String getEmployeeId() { 
        return employeeId; 
    }

    public void setEmployeeId(String employeeId) { 
        this.employeeId = employeeId; 
    }

    public String getLeaveType() { 
        return leaveType; 
    }

    public void setLeaveType(String leaveType) { 
        this.leaveType = leaveType; 
    }

    public LocalDate getStartDate() { 
        return startDate; 
    }

    public void setStartDate(LocalDate startDate) { 
        this.startDate = startDate; 
    }

    public LocalDate getEndDate() { 
        return endDate; 
    }

    public void setEndDate(LocalDate endDate) { 
        this.endDate = endDate; 
    }

    public String getStatus() { 
        return status; 
    }

    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getReason() { 
        return reason; 
    }

    public void setReason(String reason) { 
        this.reason = reason; 
    }

    // Calculate days between start and end date (inclusive)
    public long getDaysRequested() {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "employeeId='" + employeeId + '\'' +
                ", leaveType='" + leaveType + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", daysRequested=" + getDaysRequested() +
                '}';
    }
}