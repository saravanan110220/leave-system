package com.company.leavemanagement;

import java.time.LocalDate;
import java.util.*;

public class LeaveManager {
    private Map<String, Integer> leaveBalances; // employeeId -> remaining days
    private List<LeaveRequest> leaveRequests;
    
    public LeaveManager() {
        leaveBalances = new HashMap<>();
        leaveRequests = new ArrayList<>();
        // Initialize with default 20 days per employee
        initializeLeaveBalances();
    }

    private void initializeLeaveBalances() {
        leaveBalances.put("EMP001", 20);
        leaveBalances.put("EMP002", 20);
        leaveBalances.put("EMP003", 20);
    }

    // Apply for leave
    public LeaveRequest applyForLeave(String employeeId, String leaveType, 
                                       LocalDate startDate, LocalDate endDate, 
                                       String reason) {
        LeaveRequest request = new LeaveRequest(employeeId, leaveType, startDate, endDate, reason);
        leaveRequests.add(request);
        return request;
    }

    // Approve leave
    public boolean approveLeavRequest(String employeeId, LeaveRequest request) {
        if (!leaveBalances.containsKey(employeeId)) {
            return false;
        }

        // Check for conflicts
        if (hasConflict(employeeId, request.getStartDate(), request.getEndDate())) {
            return false;
        }

        long daysRequested = request.getDaysRequested();
        int balanceAvailable = leaveBalances.get(employeeId);

        if (balanceAvailable >= daysRequested) {
            request.setStatus("APPROVED");
            leaveBalances.put(employeeId, balanceAvailable - (int) daysRequested);
            return true;
        }
        return false;
    }

    // Reject leave
    public boolean rejectLeaveRequest(String employeeId, LeaveRequest request) {
        request.setStatus("REJECTED");
        return true;
    }

    // Check for overlapping leave dates
    private boolean hasConflict(String employeeId, LocalDate startDate, LocalDate endDate) {
        for (LeaveRequest request : leaveRequests) {
            if (request.getEmployeeId().equals(employeeId) && 
                request.getStatus().equals("APPROVED")) {
                
                if (!(endDate.isBefore(request.getStartDate()) || 
                      startDate.isAfter(request.getEndDate()))) {
                    return true;
                }
            }
        }
        return false;
    }

    // Get leave balance
    public int getLeaveBalance(String employeeId) {
        return leaveBalances.getOrDefault(employeeId, 0);
    }

    // Get all leave requests
    public List<LeaveRequest> getLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    public void addEmployee(String employeeId, int leaveBalance) {
        leaveBalances.put(employeeId, leaveBalance);
    }
}