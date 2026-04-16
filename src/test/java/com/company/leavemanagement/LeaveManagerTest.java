package com.company.leavemanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class LeaveManagerTest {
    private LeaveManager leaveManager;
    private LocalDate today;

    @BeforeEach
    public void setUp() {
        leaveManager = new LeaveManager();
        today = LocalDate.now();
    }

    // Test 1: Successful leave approval
    @Test
    public void testApproveLeaveWithSufficientBalance() {
        String employeeId = "EMP001";
        LocalDate startDate = today.plusDays(5);
        LocalDate endDate = today.plusDays(7);

        LeaveRequest request = leaveManager.applyForLeave(
            employeeId, "ANNUAL", startDate, endDate, "Vacation"
        );

        boolean approved = leaveManager.approveLeavRequest(employeeId, request);
        assertTrue(approved, "Leave should be approved with sufficient balance");
        assertEquals("APPROVED", request.getStatus());
        assertEquals(17, leaveManager.getLeaveBalance(employeeId)); // 20 - 3 days
    }

    // Test 2: Reject leave due to insufficient balance
    @Test
    public void testRejectLeaveWithInsufficientBalance() {
        String employeeId = "EMP001";
        LocalDate startDate = today.plusDays(5);
        LocalDate endDate = today.plusDays(28); // 24 days requested > 20 available

        LeaveRequest request = leaveManager.applyForLeave(
            employeeId, "ANNUAL", startDate, endDate, "Long Vacation"
        );

        boolean approved = leaveManager.approveLeavRequest(employeeId, request);
        assertFalse(approved, "Leave should be rejected due to insufficient balance");
        assertEquals("PENDING", request.getStatus());
        assertEquals(20, leaveManager.getLeaveBalance(employeeId)); // Balance unchanged
    }

    // Test 3: Detect conflicting leave dates
    @Test
    public void testRejectLeaveWithConflictingDates() {
        String employeeId = "EMP002";
        
        // First leave request
        LocalDate startDate1 = today.plusDays(10);
        LocalDate endDate1 = today.plusDays(15);
        LeaveRequest request1 = leaveManager.applyForLeave(
            employeeId, "ANNUAL", startDate1, endDate1, "First Leave"
        );
        leaveManager.approveLeavRequest(employeeId, request1);

        // Second conflicting request (overlaps with first)
        LocalDate startDate2 = today.plusDays(12);
        LocalDate endDate2 = today.plusDays(18);
        LeaveRequest request2 = leaveManager.applyForLeave(
            employeeId, "ANNUAL", startDate2, endDate2, "Conflicting Leave"
        );

        boolean approved = leaveManager.approveLeavRequest(employeeId, request2);
        assertFalse(approved, "Leave should be rejected due to conflicting dates");
        assertEquals("PENDING", request2.getStatus());
    }

    // Test 4: Approve non-conflicting consecutive leave
    @Test
    public void testApproveNonConflictingConsecutiveLeave() {
        String employeeId = "EMP003";
        
        // First leave
        LocalDate startDate1 = today.plusDays(10);
        LocalDate endDate1 = today.plusDays(12);
        LeaveRequest request1 = leaveManager.applyForLeave(
            employeeId, "ANNUAL", startDate1, endDate1, "First Leave"
        );
        boolean approved1 = leaveManager.approveLeavRequest(employeeId, request1);
        assertTrue(approved1);

        // Second leave after the first ends (no conflict)
        LocalDate startDate2 = today.plusDays(13);
        LocalDate endDate2 = today.plusDays(15);
        LeaveRequest request2 = leaveManager.applyForLeave(
            employeeId, "ANNUAL", startDate2, endDate2, "Second Leave"
        );

        boolean approved2 = leaveManager.approveLeavRequest(employeeId, request2);
        assertTrue(approved2, "Non-conflicting consecutive leave should be approved");
        assertEquals("APPROVED", request2.getStatus());
    }

    // Test 5: Reject leave for non-existing employee
    @Test
    public void testRejectLeaveForNonExistentEmployee() {
        LocalDate startDate = today.plusDays(5);
        LocalDate endDate = today.plusDays(7);

        LeaveRequest request = leaveManager.applyForLeave(
            "EMP999", "ANNUAL", startDate, endDate, "Vacation"
        );

        boolean approved = leaveManager.approveLeavRequest("EMP999", request);
        assertFalse(approved, "Leave should be rejected for non-existent employee");
    }

    // Test 6: Get leave balance
    @Test
    public void testGetLeaveBalance() {
        int balance = leaveManager.getLeaveBalance("EMP001");
        assertEquals(20, balance, "Initial leave balance should be 20");
    }

    // Test 7: Manual leave rejection
    @Test
    public void testRejectLeaveRequest() {
        String employeeId = "EMP001";
        LocalDate startDate = today.plusDays(5);
        LocalDate endDate = today.plusDays(7);

        LeaveRequest request = leaveManager.applyForLeave(
            employeeId, "ANNUAL", startDate, endDate, "Vacation"
        );

        boolean rejected = leaveManager.rejectLeaveRequest(employeeId, request);
        assertTrue(rejected);
        assertEquals("REJECTED", request.getStatus());
    }

    // Test 8: Verify leave days calculation
    @Test
    public void testLeaveDaysCalculation() {
        LocalDate startDate = today.plusDays(5);
        LocalDate endDate = today.plusDays(9); // 5 days

        LeaveRequest request = leaveManager.applyForLeave(
            "EMP001", "ANNUAL", startDate, endDate, "Vacation"
        );

        assertEquals(5, request.getDaysRequested(), "Should calculate 5 days correctly");
    }
}