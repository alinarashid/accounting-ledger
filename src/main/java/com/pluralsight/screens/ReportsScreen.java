package com.pluralsight.screens;

import com.pluralsight.LedgerApp;
import com.pluralsight.Transaction;

import java.time.LocalDate;

/**
 * ReportsScreen
 * Date reports (month to date, previous month, year to date, previous year),
 * search by vendor, and the custom search.
 */
public class ReportsScreen {

    public static void show() {
        String choice;
        do {
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
            choice = LedgerApp.scanner.nextLine();

            switch (choice) {
                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3" -> yearToDate();
                case "4" -> previousYear();
                case "5" -> searchByVendor();
                case "6" -> customSearch();
                case "0" -> System.out.println("Going back to ledger..");
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("0"));
    }

    public static void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String vendorName = LedgerApp.scanner.nextLine();

        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);

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
        LocalDate start = today.withDayOfMonth(1);
        showDateRange(start, today);
    }

    public static void previousMonth() {
        LocalDate firstOfThisMonth = LocalDate.now().withDayOfMonth(1);  // Nov 1  ← anchor
        LocalDate start = firstOfThisMonth.minusMonths(1);               // Oct 1  ← back 1 month
        LocalDate end = firstOfThisMonth.minusDays(1);                   // Oct 31 ← back 1 day
        showDateRange(start, end);
    }

    public static void yearToDate() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);

            if (transaction.getDate().getYear() == year) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\n No transactions found for: " + year);
        }
    }

    public static void previousYear() {
        LocalDate today = LocalDate.now();
        LocalDate previousYear = today.minusYears(1);
        int year = previousYear.getYear();

        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);

            if (transaction.getDate().getYear() == year) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\n No transactions found for: " + year);
        }
    }


    // Shows every transaction from start to end (both days included), newest first
    public static void showDateRange(LocalDate start, LocalDate end) {
        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);
            LocalDate date = transaction.getDate();

            if (!date.isBefore(start) && !date.isAfter(end)) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\n No transactions found from " + start + " to " + end);
        }
    }




    public static void customSearch() {
        System.out.print("Enter Start Date (yyyy-MM-dd): ");
        String startDate = LedgerApp.scanner.nextLine();
        System.out.print("Enter End Date (yyyy-MM-dd): ");
        String endDate = LedgerApp.scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = LedgerApp.scanner.nextLine();
        System.out.print("Enter Vendor Name: ");
        String vendor = LedgerApp.scanner.nextLine();
        System.out.print("Enter Amount: ");
        String amount = LedgerApp.scanner.nextLine();

        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);
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
                double amountParsed = Double.parseDouble(amount);
                if (transaction.getAmount() != amountParsed) {
                    matches = false;
                }
            }

            if (matches) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions match your search.");
        }
    }
}