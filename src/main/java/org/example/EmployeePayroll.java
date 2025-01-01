package org.example;

import java.time.LocalDate;
import java.util.List;

public class EmployeePayroll {
    private int id;
    private String name;
    private String gender;
    private LocalDate startDate;
    private double salary;
    private double basicPay;
    private double deductions;
    private double taxablePay;
    private double incomeTax;
    private double netPay;
    private List<String> departments;

    public EmployeePayroll(String name, String gender, LocalDate startDate, double salary, List<String> departments) {
        this.name = name;
        this.gender = gender;
        this.startDate = startDate;
        this.salary = salary;
        this.departments = departments;
        calculatePays();
    }

    public void calculatePays() {
        this.basicPay = salary * 0.8;
        this.deductions = salary * 0.1;
        this.taxablePay = salary - deductions;
        this.incomeTax = taxablePay * 0.2;
        this.netPay = salary - deductions - incomeTax;
    }

    public String getName() { return name; }
    public String getGender() { return gender; }
    public LocalDate getStartDate() { return startDate; }
    public double getSalary() { return salary; }
    public double getBasicPay() { return basicPay; }
    public double getDeductions() { return deductions; }
    public double getTaxablePay() { return taxablePay; }
    public double getIncomeTax() { return incomeTax; }
    public double getNetPay() { return netPay; }
    public List<String> getDepartments() { return departments; }
}
