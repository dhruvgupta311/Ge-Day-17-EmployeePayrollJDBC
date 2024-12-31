package org.example;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EmployeePayrollService service = EmployeePayrollService.getInstance();

        // Define the start and end dates for the range
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Fetch the employees who joined within this range
        List<EmployeePayroll> employees = service.getEmployeesByJoinDateRange(startDate, endDate);

        // Print out the employees
        if (employees.isEmpty()) {
            System.out.println("No employees found in this date range.");
        } else {
            for (EmployeePayroll employee : employees) {
                System.out.println(employee);
            }
        }
    }
}
