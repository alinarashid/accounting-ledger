package com.pluralsight.screens;

// LedgerApp and Transaction live in com.pluralsight, a different package
import com.pluralsight.LedgerApp;
import com.pluralsight.Transaction;

import java.time.LocalDate;

/**
 * ReportsScreen
 * Date reports (month to date, previous month, year to date, previous year),
 * search by vendor, and the custom search.
 */
public class ReportsScreen {

    // * Menu loop: keeps showing until the user picks 0
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
            // the shared Scanner
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

    // ===== Date reports =====
    // * Each report only works out its start and end date, then showDateRange() does the rest

    // 1st of this month -> today
    // e.g. today 2026-11-03: shows 2026-11-01 to 2026-11-03
    public static void monthToDate() {
        LocalDate today = LocalDate.now();             // 2026-11-03
        LocalDate start = today.withDayOfMonth(1);     // 2026-11-01
        showDateRange(start, today);
    }

    // 1st of last month -> last day of last month
    // e.g. today 2026-11-03: shows 2026-10-01 to 2026-10-31
    public static void previousMonth() {
        LocalDate firstOfThisMonth = LocalDate.now().withDayOfMonth(1);  // 2026-11-01 (the anchor)
        LocalDate start = firstOfThisMonth.minusMonths(1);   // 2026-10-01
        LocalDate end = firstOfThisMonth.minusDays(1);       // 2026-10-31
        // ! January works too: anchor 2027-01-01 -> start 2026-12-01, end 2026-12-31
        // ! No need to know month lengths: 2027-03-01 minus 1 day = 2027-02-28
        showDateRange(start, end);
    }

    // Jan 1 of this year -> today
    // e.g. today 2026-11-03: shows 2026-01-01 to 2026-11-03
    public static void yearToDate() {
        LocalDate today = LocalDate.now();             // 2026-11-03
        LocalDate start = today.withDayOfYear(1);      // 2026-01-01
        showDateRange(start, today);
    }

    // Jan 1 -> Dec 31 of last year
    // e.g. today 2026-11-03: shows 2025-01-01 to 2025-12-31
    public static void previousYear() {
        int lastYear = LocalDate.now().getYear() - 1;        // 2026 - 1 = 2025
        LocalDate start = LocalDate.of(lastYear, 1, 1);       // 2025-01-01
        LocalDate end = LocalDate.of(lastYear, 12, 31);       // 2025-12-31
        showDateRange(start, end);
    }

    // Shows every transaction from start to end (both days included), newest first
    // e.g. start 2026-10-01, end 2026-10-31:
    //   2026-10-01 shown (start day)   2026-10-31 shown (end day)
    //   2026-09-30 skipped (too early) 2026-11-01 skipped (too late)
    public static void showDateRange(LocalDate start, LocalDate end) {
        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);
            LocalDate date = transaction.getDate();

            // ? Why "not before" instead of "after"? So the start and end days themselves count
            if (!date.isBefore(start) && !date.isAfter(end)) {
                System.out.println(transaction);
                found = true;
            }
        }

        // ! Must be AFTER the loop, or the message prints once per transaction
        if (!found) {
            System.out.println("\n No transactions found from " + start + " to " + end);
        }
    }

    // ===== Searches =====

    // Shows every transaction for one vendor (upper/lowercase doesn't matter)
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

    // Bonus: filter by any mix of start date, end date, description, vendor and amount
    // * A blank answer skips that filter
    public static void customSearch() {
        // ? Why nextLine() for everything? So we can tell when the user left it blank
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

            // * Starts as a match, any filter it fails flips it to false
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

            // "contains" so "coffee" finds "Coffee beans 20lb"
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

            // ! Exact match, so payments need the minus sign (-320 finds it, 320 doesn't)
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