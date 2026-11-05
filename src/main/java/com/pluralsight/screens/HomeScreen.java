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

        String description = askForText("Enter description: ");
        String vendor = askForText("Enter vendor: ");
        double amount = askForAmount();

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
    }

    // * Keeps asking until the user types something (blank isn't allowed)
    // ! A | would break the CSV (it's our column separator), so swap it for a -
    public static String askForText(String question) {
        while (true) {
            System.out.print(question);
            String text = LedgerApp.scanner.nextLine().trim().replace("|", "-");

            if (text.isBlank()) {
                System.out.println("This can't be empty. Try again.");
            } else {
                return text;   // got something: hand it back
            }
        }
    }

    // * Keeps asking until the user types a real number that isn't 0
    // ? Why nextLine() + parseDouble instead of nextDouble()? "abc" would crash nextDouble(),
    //   and nextLine() leaves no leftover Enter behind
    public static double askForAmount() {
        while (true) {
            System.out.print("Enter amount: ");
            String input = LedgerApp.scanner.nextLine().trim();

            try {
                double amount = Double.parseDouble(input);   // "12.50" -> 12.5, "abc" -> error

                if (amount == 0) {
                    System.out.println("Amount can't be 0. Try again.");
                } else {
                    return amount;   // a good number: leave the loop and hand it back
                }

            } catch (NumberFormatException e) {
                // runs when the text isn't a number, like "abc" or "$12"
                System.out.println("Please enter a number, like 12.50");
            }
        }
    }
}