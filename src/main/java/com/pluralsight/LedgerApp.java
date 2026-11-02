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
                case "P"-> makePayment();
                case "L" -> ledgerScreen();
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


    public static void makePayment(){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        double fixedAmount = -Math.abs(amount);

        Transaction transaction = new Transaction(today,now,description,vendor,fixedAmount);

        FileManager.saveTransactions(transaction);
        transactions.add(transaction);

        System.out.println("Transaction added successfully.");



        scanner.nextLine();



    }


    public static void ledgerScreen() {
        String choice;
        do{
            System.out.println("""
        ===== LEDGER =====
        A) All Entries
        D) Deposits
        P) Payments
        R) Reports
        H) Home
        """);
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A" -> showAllTransactions();
                case "D"-> System.out.println("Deposits: Coming soon");
                case "P" -> System.out.println("Payments: Coming soon");
                case "R" -> System.out.println("Reports: Coming soon");
                case "H" -> System.out.println("Going home..");
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("H"));
    }


    public static void showAllTransactions(){
        for ( int i = transactions.size() - 1 ; i >= 0 ; i-- ){
            Transaction transaction = transactions.get(i);
            System.out.println(transaction);
        }

    }
}
