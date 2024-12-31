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

    // Method to get statistical analysis by gender (sum, avg, min, max, count)
    public void getEmployeeStatisticsByGender() {
        String query = "SELECT gender, SUM(salary) AS total_salary, AVG(salary) AS avg_salary, " +
                "MIN(salary) AS min_salary, MAX(salary) AS max_salary, COUNT(*) AS total_count " +
                "FROM employee_payroll GROUP BY gender";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double totalSalary = resultSet.getDouble("total_salary");
                double avgSalary = resultSet.getDouble("avg_salary");
                double minSalary = resultSet.getDouble("min_salary");
                double maxSalary = resultSet.getDouble("max_salary");
                int totalCount = resultSet.getInt("total_count");

                // Print the statistics for each gender
                System.out.println("Gender: " + gender);
                System.out.println("Total Salary: " + totalSalary);
                System.out.println("Average Salary: " + avgSalary);
                System.out.println("Minimum Salary: " + minSalary);
                System.out.println("Maximum Salary: " + maxSalary);
                System.out.println("Total Employees: " + totalCount);
                System.out.println("--------------------------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
