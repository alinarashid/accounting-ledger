package com.pluralsight.screens;

// LedgerApp and Transaction live in com.pluralsight, a different package
import com.pluralsight.theme.CafeTheme;
import com.pluralsight.LedgerApp;
import com.pluralsight.Transaction;

/**
 * LedgerScreen
 * Shows the transactions, newest first: all of them, only deposits,
 * or only payments. Also opens the reports screen.
 */
public class LedgerScreen {

    // * Menu loop: keeps showing until the user picks H
    public static void show() {
        String choice;
        do {
            CafeTheme.printBoard("LEDGER",
                    new String[]{"A", "D", "P", "R", "H"},
                    new String[]{"All entries", "Deposits", "Payments", "Reports", "Home"},
                    null);
            CafeTheme.printPrompt();
            choice = LedgerApp.scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A" -> showTransactions("ALL");
                case "D" -> showTransactions("DEPOSITS");
                case "P" -> showTransactions("PAYMENTS");
                case "R" -> ReportsScreen.show();
                // ! Don't call HomeScreen.show() here: just let this loop end and we go back home
                case "H" -> System.out.println("Going home..");
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("H"));
    }

    // Shows "ALL", "DEPOSITS" (amount > 0) or "PAYMENTS" (amount < 0)
    public static void showTransactions(String type) {
        // * Newest first: start at the LAST index and walk backwards to 0

        CafeTheme.printHeaderBar("LEDGER  ·  " + type + "  ·  newest first");
        CafeTheme.printTableHeader();

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
                System.out.println(transaction);
            }
        }
    }
}