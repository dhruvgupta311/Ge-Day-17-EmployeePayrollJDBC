package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Instantiate the EmployeePayrollService (Singleton)
        EmployeePayrollService payrollService = EmployeePayrollService.getInstance();

        // Step 1: Fetch and display all employees
        System.out.println("All Employees:");
        List<EmployeePayroll> employees = payrollService.getAllEmployees();
        for (EmployeePayroll employee : employees) {
            System.out.println(employee);
        }

        // Step 2: Update salary for Terisa
        System.out.println("\nUpdating salary for Terisa...");
        payrollService.updateEmployeeSalary("Terisa", 3000000.00);

        // Step 3: Verify the update
        EmployeePayroll updatedEmployee = payrollService.getEmployeeByName("Terisa");
        System.out.println("\nUpdated Employee Details:");
        System.out.println(updatedEmployee);
    }
}
