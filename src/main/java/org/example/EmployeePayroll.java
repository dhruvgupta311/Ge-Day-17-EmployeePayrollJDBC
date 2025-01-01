package org.example;

import java.time.LocalDate;
import java.util.List;

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
    private List<String> departments; // List to hold departments

    // Constructor
    public EmployeePayroll(int id, String name, String gender, double salary, LocalDate startDate, List<String> departments) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.startDate = startDate;
        this.departments = departments;
        calculatePays();
    }

    // Getter and Setter methods
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; calculatePays(); }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public double getBasicPay() { return basicPay; }
    public void setBasicPay(double basicPay) { this.basicPay = basicPay; }
    public double getDeductions() { return deductions; }
    public void setDeductions(double deductions) { this.deductions = deductions; }
    public double getTaxablePay() { return taxablePay; }
    public void setTaxablePay(double taxablePay) { this.taxablePay = taxablePay; }
    public double getIncomeTax() { return incomeTax; }
    public void setIncomeTax(double incomeTax) { this.incomeTax = incomeTax; }
    public double getNetPay() { return netPay; }
    public void setNetPay(double netPay) { this.netPay = netPay; }
    public List<String> getDepartments() { return departments; }
    public void setDepartments(List<String> departments) { this.departments = departments; }

    // Calculate pay breakdown
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
                ", startDate=" + startDate +
                ", basicPay=" + basicPay +
                ", deductions=" + deductions +
                ", taxablePay=" + taxablePay +
                ", incomeTax=" + incomeTax +
                ", netPay=" + netPay +
                ", departments=" + departments +
                '}';
    }
}
