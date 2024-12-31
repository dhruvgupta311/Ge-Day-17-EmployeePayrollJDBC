package org.example;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        EmployeePayrollService service = EmployeePayrollService.getInstance();

        // Fetch and display statistics for male and female employees
        service.getEmployeeStatisticsByGender();
    }
}
