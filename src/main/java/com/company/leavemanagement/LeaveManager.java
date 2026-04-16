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

    // ===== ADD THIS MAIN METHOD =====
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   Leave Management System - v1.0               ║");
        System.out.println("║   Author: Saravanan                            ║");
        System.out.println("║   Date: 2024-04-16                             ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
        
        // Initialize the Leave Manager
        LeaveManager manager = new LeaveManager();
        System.out.println("✅ Leave Management System initialized successfully!");
        System.out.println();
        
        // Display initial leave balances
        System.out.println("📊 Initial Leave Balances:");
        System.out.println("   EMP001: " + manager.getLeaveBalance("EMP001") + " days");
        System.out.println("   EMP002: " + manager.getLeaveBalance("EMP002") + " days");
        System.out.println("   EMP003: " + manager.getLeaveBalance("EMP003") + " days");
        System.out.println();
        
        // Create sample leave requests
        System.out.println("📝 Processing leave requests...");
        LeaveRequest req1 = manager.applyForLeave("EMP001", "VACATION", 
            LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 5), "Summer vacation");
        System.out.println("   Leave request created for EMP001 (2024-05-01 to 2024-05-05)");
        
        // Approve the leave request
        boolean approved = manager.approveLeavRequest("EMP001", req1);
        if (approved) {
            System.out.println("   ✅ Leave request APPROVED");
            System.out.println("   Remaining balance: " + manager.getLeaveBalance("EMP001") + " days");
        }
        System.out.println();
        
        System.out.println("🚀 Leave Management System is running successfully!");
        System.out.println();
    }
}
