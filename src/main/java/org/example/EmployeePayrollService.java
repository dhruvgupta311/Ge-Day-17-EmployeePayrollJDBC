package org.example;

import java.sql.*;
import java.util.List;

public class EmployeePayrollService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("MY_APP_PASSWORD");
    private int getDepartmentIdByName(String departmentName, Connection connection) throws SQLException {
        String query = "SELECT department_id FROM department WHERE department_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("department_id");
                } else {
                    throw new SQLException("Department not found: " + departmentName);
                }
            }
        }
    }

    public void addEmployeeWithPayroll(EmployeePayroll employee) throws SQLException {
        String insertEmployeeSQL = "INSERT INTO employee (name, gender, start_date) VALUES (?, ?, ?)";
        String insertPayrollSQL = "INSERT INTO employee_payroll (employee_id, salary, basic_pay, deductions, taxable_pay, income_tax, net_pay) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertDepartmentSQL = "INSERT INTO employee_department (employee_id, department_id) VALUES (?, ?)";
        String getDepartmentIdSQL = "SELECT department_id FROM department WHERE department_name = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            // Insert employee
            int employeeId;
            try (PreparedStatement employeeStmt = connection.prepareStatement(insertEmployeeSQL, Statement.RETURN_GENERATED_KEYS)) {
                employeeStmt.setString(1, employee.getName());
                employeeStmt.setString(2, employee.getGender());
                employeeStmt.setDate(3, Date.valueOf(employee.getStartDate()));
                employeeStmt.executeUpdate();

                try (ResultSet rs = employeeStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        employeeId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve employee ID.");
                    }
                }
            }

            // Insert payroll
            try (PreparedStatement payrollStmt = connection.prepareStatement(insertPayrollSQL)) {
                payrollStmt.setInt(1, employeeId);
                payrollStmt.setDouble(2, employee.getSalary());
                payrollStmt.setDouble(3, employee.getBasicPay());
                payrollStmt.setDouble(4, employee.getDeductions());
                payrollStmt.setDouble(5, employee.getTaxablePay());
                payrollStmt.setDouble(6, employee.getIncomeTax());
                payrollStmt.setDouble(7, employee.getNetPay());
                payrollStmt.executeUpdate();
            }

            // Insert departments
            try (PreparedStatement departmentStmt = connection.prepareStatement(insertDepartmentSQL);
                 PreparedStatement getDeptStmt = connection.prepareStatement(getDepartmentIdSQL)) {
                for (String department : employee.getDepartments()) {
                    getDeptStmt.setString(1, department);
                    try (ResultSet rs = getDeptStmt.executeQuery()) {
                        if (rs.next()) {
                            int departmentId = rs.getInt("department_id");
                            departmentStmt.setInt(1, employeeId);
                            departmentStmt.setInt(2, departmentId);
                            departmentStmt.addBatch();
                        } else {
                            throw new SQLException("Department not found: " + department);
                        }
                    }
                }
                departmentStmt.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            throw new SQLException("Transaction failed. Rolling back.", e);
        }
    }
}
