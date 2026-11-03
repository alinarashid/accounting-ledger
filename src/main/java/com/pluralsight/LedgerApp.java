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
                case "D"-> showDeposits();
                case "P" -> showPayments();
                case "R" -> reportsScreen();
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

    public static void showDeposits(){
        for ( int i = transactions.size() - 1 ; i >= 0 ; i-- ){
            Transaction transaction = transactions.get(i);

            if (transaction.getAmount() > 0){
                System.out.println(transaction);
            }

        }

    }

    public static void showPayments(){
        for ( int i = transactions.size() - 1 ; i >= 0 ; i-- ){
            Transaction transaction = transactions.get(i);

            if (transaction.getAmount() <0){
                System.out.println(transaction);
            }
        }

    }

    public static void reportsScreen(){
        String choice;
        do{
            System.out.println("""
        
        ===== REPORTS =====
        1) Month To Date
        2) Previous Month
        3) Year To Date
        4) Previous Year
        5) Search by Vendor
        6) Custom Search
        0) Back
        """);
            System.out.print("Choose an option: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3"-> yearToDate();
                case "4" -> previousYear();
                case "5" -> searchByVendor();
                case "6" -> customSearch();
                case "0" -> System.out.println("Going back to ledger.. ");
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("0"));

    }

    public static void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String vendorName = scanner.nextLine();

        boolean found = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getVendor().equalsIgnoreCase(vendorName)) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\n No transactions found for: " + vendorName);
        }
    }

    public static void monthToDate() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        boolean found = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getDate().getYear() == year && transaction.getDate().getMonthValue() == month) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("\n No transactions found for: " + today.getYear() + "-" + today.getMonthValue());
        }

    }

    public static void previousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);

        int year = previousMonth.getYear();
        int month = previousMonth.getMonthValue();

        boolean found = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getDate().getYear() == year && transaction.getDate().getMonthValue() == month) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("\n No transactions found for: " + previousMonth.getYear() + "-" + previousMonth.getMonthValue());
        }

    }


    public static void yearToDate() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        boolean found = false;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (transaction.getDate().getYear() == year ) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("\n No transactions found for: " + today.getYear());
        }

    }

    public static void previousYear() {
        LocalDate today = LocalDate.now();
        LocalDate previousYear = today.minusYears(1);
        int year = previousYear.getYear();

        boolean found = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().getYear() == year ) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("\n No transactions found for: " + year);
        }

    }

    public static void customSearch() {
        System.out.print("Enter Start Date (yyyy-MM-dd): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter End Date (yyyy-MM-dd): ");
        String endDate = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Vendor Name: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter Amount: ");
        String amount = scanner.nextLine();

        boolean found = false;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            boolean matches = true;

            if (!startDate.isBlank()) {
                LocalDate from = LocalDate.parse(startDate);
                if (transaction.getDate().isBefore(from)) {
                    matches = false;
                }
            }

            if (!endDate.isBlank()) {
                LocalDate to = LocalDate.parse(endDate);
                if (transaction.getDate().isAfter(to)) {
                    matches = false;
                }
            }


            if (!description.isBlank()) {
                description = description.trim();
                if (!transaction.getDescription().toLowerCase().contains(description.toLowerCase())) {
                    matches = false;
                }
            }

            if (!vendor.isBlank()) {
                vendor = vendor.trim();
                if (!transaction.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                    matches = false;
                }
            }

            if (!amount.isBlank()) {
                double amountPrased = Double.parseDouble(amount);
                if (transaction.getAmount() != amountPrased) {
                    matches = false;
                }
            }

            if (matches) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found){
            System.out.println("No transactions match your search.");

        }
    }


}