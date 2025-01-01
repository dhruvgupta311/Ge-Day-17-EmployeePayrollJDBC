package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        EmployeePayrollService service = new EmployeePayrollService();

        EmployeePayroll employee = new EmployeePayroll(
                "John Doe",
                "M",
                LocalDate.of(2023, 1, 15),
                60000,
                Arrays.asList("IT", "HR")
        );

        try {
            service.addEmployeeWithPayroll(employee);
            System.out.println("Employee added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
