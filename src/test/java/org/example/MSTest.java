package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MSTest {

    private EmployeePayrollService employeePayrollService;

    @BeforeEach
    public void setup() {
        employeePayrollService = EmployeePayrollService.getInstance();
    }

    @Test
    public void testSalaryUpdate() {
        // Assuming we already have an employee named "John" in the database
        String employeeName = "John Doe";

        // Fetch employee details by name before the update
        EmployeePayroll employee = employeePayrollService.getEmployeeByName(employeeName);
        assertNotNull(employee, "Employee should not be null");

        // Store the original salary for later validation
        double originalSalary = employee.getSalary();

        // Update salary and check if the details are updated
        double newSalary = 75002.0;
        employeePayrollService.updateEmployeeSalary(employeeName, newSalary);

        // Re-fetch employee details after the update
        EmployeePayroll updatedEmployee = employeePayrollService.getEmployeeByName(employeeName);
        assertNotNull(updatedEmployee, "Updated employee should not be null");

        // Log the original and updated salary for debugging
        System.out.println("Original Salary: " + originalSalary);
        System.out.println("Updated Salary: " + updatedEmployee.getSalary());

        // Ensure the updated salary is correct (this assertion checks if salary has actually been updated)
        assertEquals(newSalary, updatedEmployee.getSalary(), "Salary should be updated");

        // Ensure the updated salary is different from the original salary
        assertNotEquals(originalSalary, updatedEmployee.getSalary(), "Salary should have been updated");
    }    }

