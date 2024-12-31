package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MSTest {
    @Test
    public void testSalaryUpdate() {
        EmployeePayrollService payrollService = new EmployeePayrollService();
        payrollService.updateEmployeeSalary("Terisa", 3000000.00);
        EmployeePayroll updatedEmployee = payrollService.getEmployeeByName("Terisa");

        assertEquals(3000000.00, updatedEmployee.getSalary());
        System.out.println("Test Passed: Salary updated correctly.");
    }
}
