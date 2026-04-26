package ui;

import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner;

    public ConsoleMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void showMainMenu() {
        System.out.println("=============================");
        System.out.println("   Pet Clinic Appointment System");
        System.out.println("=============================");
        System.out.println("[1] Book Appointment");
        System.out.println("[2] View Appointments");
        System.out.println("[3] Manage Pets");
        System.out.println("[4] Billing");
        System.out.println("[0] Exit");
        System.out.print("Choose an option: ");
    }

    public int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public String prompt(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }
}