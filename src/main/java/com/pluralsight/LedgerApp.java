package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        ArrayList<Transaction> transactions = FileManager.loadTransactions();

//        for ( Transaction transaction : transactions) {
//            System.out.println(transaction);
//        }

        homeScreen();
    }

    public static void homeScreen() {
        String choice;
        do{
            System.out.println("""
        ===== HOME =====
        D) Add Deposit
        P) Make Payment
        L) Ledger
        X) Exit
        """);
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D" -> System.out.println("Deposit: Coming soon");
                case "P"-> System.out.println("Make Payment: Coming soon");
                case "L" -> System.out.println("Ledger: Coming soon");
                case "X" -> System.out.println("Exiting..");
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("X"));
    }
}
