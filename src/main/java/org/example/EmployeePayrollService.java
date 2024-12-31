package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("MY_APP_PASSWORD");

    // Register the MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Ensure the MySQL driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL Driver not found!", e);
        }
    }

    public void updateEmployeeSalary(String name, double newSalary) {
        String updateQuery = "UPDATE employee_payroll SET salary = ?, basic_pay = ?, deductions = ?, taxable_pay = ?, income_tax = ?, net_pay = ? WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Calculate pay details
            double basicPay = newSalary * 0.8;
            double deductions = newSalary * 0.1;
            double taxablePay = newSalary - deductions;
            double incomeTax = taxablePay * 0.2;
            double netPay = newSalary - deductions - incomeTax;

            // Set parameters
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
            } else {
                System.out.println("No employee found with the name " + name);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public EmployeePayroll getEmployeeByName(String name) {
        String query = "SELECT * FROM employee_payroll WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new EmployeePayroll(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getDouble("salary"),
                        resultSet.getDate("start_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Employee not found
    }

    public List<EmployeePayroll> getAllEmployees() {
        List<EmployeePayroll> employees = new ArrayList<>();
        String query = "SELECT * FROM employee_payroll";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                employees.add(new EmployeePayroll(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getDouble("salary"),
                        resultSet.getDate("start_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }
}
