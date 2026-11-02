package com.pluralsight;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = FileManager.loadTransactions();

    public static void main(String[] args) {


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
                case "D" -> addDeposit();
                case "P"-> System.out.println("Make Payment: Coming soon");
                case "L" -> System.out.println("Ledger: Coming soon");
                case "X" -> System.out.println("Exiting..");
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("X"));
    }

    public static void addDeposit(){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        if ( amount > 0){
            Transaction transaction = new Transaction(today,now,description,vendor,amount);

            FileManager.saveTransactions(transaction);
            transactions.add(transaction);

            System.out.println("Transaction added successfully.");


        } else {
            System.out.println("Invalid amount.");
        }
        scanner.nextLine();



    }
}
