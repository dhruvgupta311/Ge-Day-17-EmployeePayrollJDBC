package org.example;

import java.time.LocalDate;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        EmployeePayrollService service = new EmployeePayrollService();

        EmployeePayroll employee = new EmployeePayroll(
                0,
                "John Doe",
                "M",
                50000,
                LocalDate.of(2023, 1, 1),
                Arrays.asList("HR", "Finance")
        );

        service.addEmployeeWithPayroll(employee);
    }
}
