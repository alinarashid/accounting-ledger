package com.pluralsight.screens;

// My own classes from other packages
import com.pluralsight.theme.CafeTheme;
import com.pluralsight.LedgerApp;
import com.pluralsight.Transaction;

// Java's built-in date tools
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ReportsScreen
 * Date reports (month to date, previous month, year to date, previous year),
 * search by vendor, and the custom search.
 */
public class ReportsScreen {

    // Menu loop: keeps showing until the user picks 0
    public static void show() {
        String choice;
        do {
            CafeTheme.printBoard("REPORTS",
                    new String[]{"1", "2", "3", "4", "5", "6", "0"},
                    new String[]{"Month to date", "Previous month", "Year to date",
                            "Previous year", "Search by vendor", "Custom search", "Back"},
                    null);
            CafeTheme.printPrompt();
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
    // Each report only picks a start and end date, then showDateRange() does the rest

    // 1st of this month -> today   (e.g. Nov 1 -> Nov 6)
    public static void monthToDate() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        CafeTheme.printHeaderBar("MONTH TO DATE");
        showDateRange(start, today);

    }

    // 1st -> last day of last month   (e.g. Oct 1 -> Oct 31)
    public static void previousMonth() {
        LocalDate firstOfThisMonth = LocalDate.now().withDayOfMonth(1);  // the anchor: Nov 1
        LocalDate start = firstOfThisMonth.minusMonths(1);   // Oct 1
        LocalDate end = firstOfThisMonth.minusDays(1);        // Oct 31

        // Works for any month length and for January (Jan 1 -> Dec 1 to Dec 31)
        CafeTheme.printHeaderBar("PREVIOUS MONTH");
        showDateRange(start, end);
    }

    // Jan 1 -> today
    public static void yearToDate() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfYear(1);
        CafeTheme.printHeaderBar("YEAR TO DATE");
        showDateRange(start, today);
    }

    // Jan 1 -> Dec 31 of last year
    public static void previousYear() {
        int lastYear = LocalDate.now().getYear() - 1;
        LocalDate start = LocalDate.of(lastYear, 1, 1);
        LocalDate end = LocalDate.of(lastYear, 12, 31);
        CafeTheme.printHeaderBar("PREVIOUS YEAR");
        showDateRange(start, end);
    }

    // Shows every transaction from start to end (both days included), newest first
    public static void showDateRange(LocalDate start, LocalDate end) {
        // e.g. "Nov 1, 2026  →  Nov 5, 2026"
        DateTimeFormatter cuteDate = DateTimeFormatter.ofPattern("MMM d, yyyy");
        System.out.println(CafeTheme.GRAY + start.format(cuteDate) + "  →  " + end.format(cuteDate) + CafeTheme.RESET+ "\n");
        CafeTheme.printTableHeader();
        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);
            LocalDate date = transaction.getDate();

            // "not before" and "not after" so the start and end days count too
            if (!date.isBefore(start) && !date.isAfter(end)) {
                System.out.println(transaction);
                found = true;
            }
        }

        // After the loop, so the message prints once (not once per transaction)
        if (!found) {
            System.out.println("\n No transactions found from " + start + " to " + end);
        }
    }

    // ===== Searches =====

    // Every transaction for one vendor (upper/lowercase doesn't matter)
    public static void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String vendorName = LedgerApp.scanner.nextLine();

        CafeTheme.printHeaderBar("SEARCH BY VENDOR  ·  " + vendorName);
        CafeTheme.printTableHeader();
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

    // Bonus: filter by any mix of dates, description, vendor and amount
    // A blank answer skips that filter
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

        // Turn the typed text into dates and a number ONCE, before the loop
        LocalDate from = null;
        LocalDate to = null;
        double amountParsed = 0;

        try {
            if (!startDate.isBlank()) {
                from = LocalDate.parse(startDate.trim());
            }
            if (!endDate.isBlank()) {
                to = LocalDate.parse(endDate.trim());
            }
            if (!amount.isBlank()) {
                amountParsed = Double.parseDouble(amount.trim());
            }
        } catch (Exception e) {
            // Typed wrong: show a message and go back to the menu instead of crashing
            System.out.println("Invalid input. Dates must look like 2026-11-04 and amounts like 12.50");
            return;
        }

        CafeTheme.printHeaderBar("CUSTOM SEARCH");
        CafeTheme.printTableHeader();
        boolean found = false;

        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);

            // Starts as a match, any filter it fails flips it to false
            boolean matches = true;

            if (!startDate.isBlank()) {
                if (transaction.getDate().isBefore(from)) {
                    matches = false;
                }
            }

            if (!endDate.isBlank()) {
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

            // Exact match, so payments need the minus sign (-320 finds it, 320 doesn't)
            if (!amount.isBlank()) {
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