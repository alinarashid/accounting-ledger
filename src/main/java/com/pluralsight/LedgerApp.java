package com.pluralsight;
// Different package, so it needs an import
import com.pluralsight.screens.HomeScreen;

// ArrayList and Scanner come from Java's built-in java.util toolbox.
import java.util.ArrayList;
import java.util.Scanner;

/**
 * LedgerApp
 * The starting point of the app.
 * It holds the data every screen shares (the Scanner and the list of transactions),
 * then opens the home screen.
 */
public class LedgerApp {

    // * Shared by all screens (public: screens are in another package, static: one copy)
    public static Scanner scanner = new Scanner(System.in);

    // ! Loaded from the CSV as soon as the app starts, before main() runs
    public static ArrayList<Transaction> transactions = FileManager.loadTransactions();

    public static void main(String[] args) {
        // App ends when the user exits the home screen
        HomeScreen.show();
    }
}