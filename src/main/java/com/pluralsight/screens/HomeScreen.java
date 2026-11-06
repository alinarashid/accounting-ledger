package com.pluralsight.screens;

// My own classes from other packages
import com.pluralsight.theme.CafeSound;
import com.pluralsight.theme.CafeTheme;
import com.pluralsight.FileManager;
import com.pluralsight.LedgerApp;
import com.pluralsight.Transaction;

// Java's built-in date and time tools
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * HomeScreen
 * The first menu: add a deposit, make a payment, open the ledger, or exit.
 */
public class HomeScreen {

    // Menu loop: keeps showing until the user picks X
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

    // One method for both: isDeposit = true for a deposit, false for a payment
    public static void addTransaction(boolean isDeposit) {

        // Date and time come from the computer's clock
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Pink title bar: "NEW DEPOSIT" or "NEW PAYMENT"
        CafeTheme.printHeaderBar(isDeposit ? "NEW DEPOSIT" : "NEW PAYMENT");

        String description = askForText("Enter description: ");
        String vendor = askForText("Enter vendor: ");
        double amount = askForAmount();

        // Deposits are always positive, payments always negative, whatever the user types.
        double finalAmount;
        if (isDeposit) {
            finalAmount = Math.abs(amount);
            CafeSound.playDepositSound();
        } else {
            finalAmount = -Math.abs(amount);
            CafeSound.playPaymentSound();
        }
        Transaction transaction = new Transaction(today, now, description, vendor, finalAmount);

        // Save to both: the file keeps it for next time, the list shows it right now
        FileManager.saveTransaction(transaction);
        LedgerApp.transactions.add(transaction);

        CafeTheme.printReceipt(transaction);
    }

    // Keeps asking until the user types something (blank isn't allowed)
    // A | would break the CSV (it's our column separator), so swap it for a -
    public static String askForText(String question) {
        while (true) {
            System.out.print(question);
            String text = LedgerApp.scanner.nextLine().trim().replace("|", "-");

            if (text.isBlank()) {
                System.out.println("This can't be empty. Try again.");
            } else {
                return text;
            }
        }
    }

    // Keeps asking until the user types a real number that isn't 0
    // nextLine() + parseDouble so "abc" shows a message instead of crashing
    public static double askForAmount() {
        while (true) {
            System.out.print("Enter amount: ");
            String input = LedgerApp.scanner.nextLine().trim();

            try {
                double amount = Double.parseDouble(input);   // "12.50" -> 12.5, "abc" -> error

                if (amount == 0) {
                    System.out.println("Amount can't be 0. Try again.");
                } else {
                    return amount;
                }

            } catch (NumberFormatException e) {
                // the text isn't a number, like "abc" or "$12"
                System.out.println("Please enter a number, like 12.50");
            }
        }
    }
}