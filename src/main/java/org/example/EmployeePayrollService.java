package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("MY_APP_PASSWORD");

    // Singleton instance of EmployeePayrollService
    private static EmployeePayrollService instance;

    // Cache for PreparedStatement
    private static Map<String, PreparedStatement> preparedStatementCache = new HashMap<>();

    private EmployeePayrollService() {
    }

    public static EmployeePayrollService getInstance() {
        if (instance == null) {
            instance = new EmployeePayrollService();
        }
        return instance;
    }

    // Create a cached PreparedStatement to retrieve employee payroll data by name
    private PreparedStatement getEmployeePayrollStatement(Connection connection, String name) throws SQLException {
        if (!preparedStatementCache.containsKey(name)) {
            String query = "SELECT * FROM employee_payroll WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatementCache.put(name, preparedStatement);
        }
        PreparedStatement preparedStatement = preparedStatementCache.get(name);
        preparedStatement.setString(1, name);
        return preparedStatement;
    }

    // Retrieve employee payroll data by name
    public EmployeePayroll getEmployeeByName(String name) {
        String query = "SELECT * FROM employee_payroll WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new EmployeePayroll(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("gender"),
                            resultSet.getDouble("salary"),
                            resultSet.getDate("start_date").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Method to update employee salary
    public void updateEmployeeSalary(String name, double newSalary) {
        String updateQuery = "UPDATE employee_payroll SET salary = ?, basic_pay = ?, deductions = ?, taxable_pay = ?, income_tax = ?, net_pay = ? WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Begin transaction (if needed)
            connection.setAutoCommit(false);  // Disable auto-commit for explicit control

            double basicPay = newSalary * 0.8;
            double deductions = newSalary * 0.1;
            double taxablePay = newSalary - deductions;
            double incomeTax = taxablePay * 0.2;
            double netPay = newSalary - deductions - incomeTax;

            preparedStatement.setDouble(1, newSalary);
            preparedStatement.setDouble(2, basicPay);
            preparedStatement.setDouble(3, deductions);
            preparedStatement.setDouble(4, taxablePay);
            preparedStatement.setDouble(5, incomeTax);
            preparedStatement.setDouble(6, netPay);
            preparedStatement.setString(7, name);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Salary updated successfully for " + name);
                connection.commit();  // Explicitly commit the transaction
            } else {
                System.out.println("No employee found with the name " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Method to retrieve all employees
    public List<EmployeePayroll> getAllEmployees() {
        List<EmployeePayroll> employeeList = new ArrayList<>();
        String query = "SELECT * FROM employee_payroll";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                EmployeePayroll employee = new EmployeePayroll(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getDouble("salary"),
                        resultSet.getDate("start_date").toLocalDate()
                );
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }
}
