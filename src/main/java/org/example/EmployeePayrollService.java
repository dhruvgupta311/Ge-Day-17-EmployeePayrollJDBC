package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("MY_APP_PASSWORD");

    private static EmployeePayrollService instance = null;

    private EmployeePayrollService() {
        // Private constructor to implement Singleton pattern
    }

    public static EmployeePayrollService getInstance() {
        if (instance == null) {
            instance = new EmployeePayrollService();
        }
        return instance;
    }

    // Method to retrieve employees joined within a particular date range
    public List<EmployeePayroll> getEmployeesByJoinDateRange(LocalDate startDate, LocalDate endDate) {
        List<EmployeePayroll> employeeList = new ArrayList<>();
        String query = "SELECT * FROM employee_payroll WHERE start_date BETWEEN ? AND ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the start and end dates for the query
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    EmployeePayroll employee = mapResultSetToEmployee(resultSet);
                    employeeList.add(employee);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    // Helper method to map the result set to an EmployeePayroll object
    private EmployeePayroll mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String gender = resultSet.getString("gender");
        double salary = resultSet.getDouble("salary");
        LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
        return new EmployeePayroll(id, name, gender, salary, startDate);
    }
}
