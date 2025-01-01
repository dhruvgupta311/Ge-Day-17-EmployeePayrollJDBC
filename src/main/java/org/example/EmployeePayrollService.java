package org.example;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("MY_APP_PASSWORD");  // Fetch Make sure to set the password

    private static Scanner scanner = new Scanner(System.in);

    // Singleton instance
    private static EmployeePayrollService instance = null;

    private EmployeePayrollService() {
        // Private constructor to enforce Singleton pattern
    }

    public static EmployeePayrollService getInstance() {
        if (instance == null) {
            instance = new EmployeePayrollService();
        }
        return instance;
    }

    // Connect to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    // Add employee along with payroll and departments
    public void addEmployeeWithPayroll(EmployeePayroll employeePayroll) {
        String insertEmployeeSQL = "INSERT INTO employee (name, gender, start_date) VALUES (?, ?, ?)";
        String insertPayrollSQL = "INSERT INTO employee_payroll (employee_id, salary, basic_pay, deductions, taxable_pay, income_tax, net_pay) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertDepartmentSQL = "INSERT INTO employee_department (employee_id, department_id) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            // Insert into employee table
            try (PreparedStatement employeeStmt = connection.prepareStatement(insertEmployeeSQL, Statement.RETURN_GENERATED_KEYS)) {
                employeeStmt.setString(1, employeePayroll.getName());
                employeeStmt.setString(2, employeePayroll.getGender());
                employeeStmt.setDate(3, Date.valueOf(employeePayroll.getStartDate()));
                int rowsAffected = employeeStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Inserting employee failed, no rows affected.");
                }

                // Get the generated employee ID
                try (ResultSet generatedKeys = employeeStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int employeeId = generatedKeys.getInt(1);  // Get generated employee_id

                        // Insert payroll details
                        try (PreparedStatement payrollStmt = connection.prepareStatement(insertPayrollSQL)) {
                            payrollStmt.setInt(1, employeeId);
                            payrollStmt.setDouble(2, employeePayroll.getSalary());
                            payrollStmt.setDouble(3, employeePayroll.getBasicPay());
                            payrollStmt.setDouble(4, employeePayroll.getDeductions());
                            payrollStmt.setDouble(5, employeePayroll.getTaxablePay());
                            payrollStmt.setDouble(6, employeePayroll.getIncomeTax());
                            payrollStmt.setDouble(7, employeePayroll.getNetPay());
                            payrollStmt.executeUpdate();
                        }

                        // Insert employee departments
                        try (PreparedStatement departmentStmt = connection.prepareStatement(insertDepartmentSQL)) {
                            for (String departmentName : employeePayroll.getDepartments()) {
                                int departmentId = getOrCreateDepartmentId(departmentName, connection);
                                departmentStmt.setInt(1, employeeId);
                                departmentStmt.setInt(2, departmentId);
                                departmentStmt.addBatch();
                            }
                            departmentStmt.executeBatch();
                        }

                        connection.commit();
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get or create a department
    private int getOrCreateDepartmentId(String departmentName, Connection connection) throws SQLException {
        String query = "SELECT department_id FROM department WHERE department_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("department_id");
                }
            }
        }

        // Insert department if not found
        String insertQuery = "INSERT INTO department (department_name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departmentName);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert department: " + departmentName);
    }

    // Menu display
    public void showMenu() {
        System.out.println("Welcome to Payroll Service");
        System.out.println("1. Add Employee with Payroll");
        System.out.println("2. View Employee Payroll");
        System.out.println("3. Update Employee Payroll");
        System.out.println("4. Delete Employee Payroll");
        System.out.println("5. Exit");
        System.out.print("Please select an option: ");
    }

    // Handle menu options
    public void handleMenuOption(int option) {
        switch (option) {
            case 1:
                addEmployeeMenu();
                break;
            case 2:
                viewEmployeePayroll();
                break;
            case 3:
                updateEmployeePayroll();
                break;
            case 4:
                deleteEmployeePayroll();
                break;
            case 5:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, please try again.");
        }
    }

    // Add Employee and Payroll through menu
    private void addEmployeeMenu() {
        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Basic Pay: ");
        double basicPay = scanner.nextDouble();
        System.out.print("Enter Deductions: ");
        double deductions = scanner.nextDouble();
        System.out.print("Enter Taxable Pay: ");
        double taxablePay = scanner.nextDouble();
        System.out.print("Enter Income Tax: ");
        double incomeTax = scanner.nextDouble();
        System.out.print("Enter Net Pay: ");
        double netPay = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Departments (comma separated): ");
        String departmentsInput = scanner.nextLine();
        List<String> departments = List.of(departmentsInput.split(","));

        EmployeePayroll employeePayroll = new EmployeePayroll(
                0, name, gender, salary, java.time.LocalDate.parse(startDate),
                departments
        );
        employeePayroll.setBasicPay(basicPay);
        employeePayroll.setDeductions(deductions);
        employeePayroll.setTaxablePay(taxablePay);
        employeePayroll.setIncomeTax(incomeTax);
        employeePayroll.setNetPay(netPay);

        getInstance().addEmployeeWithPayroll(employeePayroll);
    }

    // View Employee Payroll details
    private void viewEmployeePayroll() {
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        // Implement query to fetch and display employee payroll
    }

    // Update Employee Payroll
    private void updateEmployeePayroll() {
        System.out.print("Enter Employee ID to update: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        // Implement update logic
    }

    // Delete Employee Payroll
    private void deleteEmployeePayroll() {
        System.out.print("Enter Employee ID to delete: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        // Implement delete logic
    }
}
