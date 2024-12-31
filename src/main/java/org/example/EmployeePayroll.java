package org.example;
import java.time.LocalDate;

public class EmployeePayroll {
    private int id;
    private String name;
    private String gender;
    private double salary;
    private LocalDate startDate;
    private double basicPay;
    private double deductions;
    private double taxablePay;
    private double incomeTax;
    private double netPay;

    public EmployeePayroll(int id, String name, String gender, double salary, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.startDate = startDate;
        calculatePays();
    }

    public void setSalary(double salary) {
        this.salary = salary;
        calculatePays();
    }
    public double getSalary() {
        return salary;
    }
    private void calculatePays() {
        this.basicPay = salary * 0.8;
        this.deductions = salary * 0.1;
        this.taxablePay = salary - deductions;
        this.incomeTax = taxablePay * 0.2;
        this.netPay = salary - deductions - incomeTax;
    }

    @Override
    public String toString() {
        return "EmployeePayroll{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", salary=" + salary +
                ", basicPay=" + basicPay +
                ", deductions=" + deductions +
                ", taxablePay=" + taxablePay +
                ", incomeTax=" + incomeTax +
                ", netPay=" + netPay +
                '}';
    }
}
