package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EmployeePayrollService service = EmployeePayrollService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            service.showMenu();
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            service.handleMenuOption(option);
        }
    }
}
