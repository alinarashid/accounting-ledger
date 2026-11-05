package com.pluralsight.screens;

// These classes live in com.pluralsight, a different package, so they need imports
import com.pluralsight.theme.CafeSound;
import com.pluralsight.theme.CafeTheme;
import com.pluralsight.FileManager;
import com.pluralsight.LedgerApp;
import com.pluralsight.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * HomeScreen
 * The first menu the user sees: add a deposit, make a payment,
 * open the ledger, or exit.
 */
public class HomeScreen {

    // * Menu loop: keeps showing until the user picks X
    public static void show() {
        String choice;
        do {
            CafeTheme.printBoard("FRONT COUNTER",
                    new String[]{"D", "P", "L", "X"},
                    new String[]{"Add deposit", "Make payment", "Ledger", "Close up shop"},
                    new String[]{"money in", "money out", "the books", "exit"});
            CafeTheme.printPrompt();

            // toUpperCase() so "d" and "D" both work
            choice = LedgerApp.scanner.nextLine().toUpperCase();

            switch (choice) {
                // true = deposit
                case "D" -> addTransaction(true);
                // false = payment
                case "P" -> addTransaction(false);
                // when the ledger loop ends, we come back here
                case "L" -> LedgerScreen.show();
                case "X" -> CafeTheme.printGoodbye();
                default -> System.out.println("Invalid choice.");
            }
        } while (!choice.equals("X"));
    }

    // Adds a deposit (isDeposit = true) or a payment (isDeposit = false)
    public static void addTransaction(boolean isDeposit) {
        // Date and time come from the computer's clock, the user doesn't type them
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        // Pink title bar: "NEW DEPOSIT" or "NEW PAYMENT"
        CafeTheme.printHeaderBar(isDeposit ? "NEW DEPOSIT" : "NEW PAYMENT");

        System.out.print("Enter description: ");
        String description = LedgerApp.scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = LedgerApp.scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = LedgerApp.scanner.nextDouble();

        // * Deposits are always positive, payments always negative, whatever the user types
        double finalAmount;
        if (isDeposit) {
            finalAmount = Math.abs(amount);
            CafeSound.playDepositSound();
        } else {
            finalAmount = -Math.abs(amount);
            CafeSound.playPaymentSound();
        }
        Transaction transaction = new Transaction(today, now, description, vendor, finalAmount);

        // ? Why both? File = kept for next time, list = shows up in the ledger right now
        FileManager.saveTransaction(transaction);
        LedgerApp.transactions.add(transaction);

        CafeTheme.printReceipt(transaction);   // pink receipt instead of a plain message

        // ! Eats the leftover Enter from nextDouble(), or the menu reads it as "Invalid choice"
        LedgerApp.scanner.nextLine();

    }
}