package org.example;

import java.sql.*;
import java.util.List;

public class EmployeePayrollService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("MY_APP_PASSWORD");

    public void addEmployeeWithPayroll(EmployeePayroll employee) {
        String insertEmployeeSQL = "INSERT INTO employee (name, gender, start_date) VALUES (?, ?, ?)";
        String insertPayrollSQL = "INSERT INTO employee_payroll (employee_id, salary, basic_pay, deductions, taxable_pay, income_tax, net_pay) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertEmployeeDepartmentSQL = "INSERT INTO employee_department (employee_id, department_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            int employeeId;
            try (PreparedStatement employeeStmt = connection.prepareStatement(insertEmployeeSQL, Statement.RETURN_GENERATED_KEYS)) {
                employeeStmt.setString(1, employee.getName());
                employeeStmt.setString(2, employee.getGender());
                employeeStmt.setDate(3, Date.valueOf(employee.getStartDate()));

                employeeStmt.executeUpdate();

                try (ResultSet generatedKeys = employeeStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employeeId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve employee ID.");
                    }
                }
            }

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

            try (PreparedStatement departmentStmt = connection.prepareStatement(insertEmployeeDepartmentSQL)) {
                for (String departmentName : employee.getDepartments()) {
                    int departmentId = getDepartmentIdByName(departmentName, connection);
                    departmentStmt.setInt(1, employeeId);
                    departmentStmt.setInt(2, departmentId);
                    departmentStmt.addBatch();
                }
                departmentStmt.executeBatch();
            }

            connection.commit();
            System.out.println("Employee added successfully with payroll and departments.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getDepartmentIdByName(String departmentName, Connection connection) throws SQLException {
        String query = "SELECT department_id FROM departments WHERE department_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("department_id");
                } else {
                    throw new SQLException("Department not found: " + departmentName);
                }
            }
        }
    }
}
