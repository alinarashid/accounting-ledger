package com.pluralsight.screens;

// My own classes from other packages
import com.pluralsight.theme.CafeTheme;
import com.pluralsight.LedgerApp;
import com.pluralsight.Transaction;

/**
 * LedgerScreen
 * Shows transactions newest first: all, deposits only, or payments only.
 * Also opens the reports screen.
 */
public class LedgerScreen {

    // Menu loop: keeps showing until the user picks H
    public static void show() {
        String choice;
        do {
            CafeTheme.printBoard("LEDGER",
                    new String[]{"A", "D", "P", "R", "H"},
                    new String[]{"All entries", "Deposits", "Payments", "Reports", "Home"},
                    null); // null = no hints on this menu
            CafeTheme.printPrompt();
            choice = LedgerApp.scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A" -> showTransactions("ALL");
                case "D" -> showTransactions("DEPOSITS");
                case "P" -> showTransactions("PAYMENTS");
                case "R" -> ReportsScreen.show();
                // No HomeScreen.show() here: ending this loop takes us back home
                case "H" -> System.out.println("Going home..");
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("H"));
    }

    // One method for all 3: "ALL", "DEPOSITS" (amount > 0) or "PAYMENTS" (amount < 0)
    public static void showTransactions(String type) {
        CafeTheme.printHeaderBar("LEDGER  ·  " + type + "  ·  newest first");
        CafeTheme.printTableHeader();

        // Newest first: start at the last index and walk back to 0
        for (int i = LedgerApp.transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = LedgerApp.transactions.get(i);

            // Starts as "don't show", flips to true if this transaction fits the type
            boolean show = false;

            if (type.equals("ALL")) {
                show = true;
            } else if (type.equals("DEPOSITS") && transaction.getAmount() > 0) {
                show = true;
            } else if (type.equals("PAYMENTS") && transaction.getAmount() < 0) {
                show = true;
            }

            if (show) {
                System.out.println(transaction); // calls Transaction's toString()
            }
        }
    }
}