package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EmployeePayrollService payrollService = new EmployeePayrollService();

        // Step 1: Fetch and display all employees
        System.out.println("All Employees:");
        for (EmployeePayroll employee : payrollService.getAllEmployees()) {
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
