package com.pluralsight;

// Screens and theme are different packages, so they need imports
import com.pluralsight.theme.CafeSound;
import com.pluralsight.theme.CafeTheme;
import com.pluralsight.screens.HomeScreen;

// ArrayList and Scanner come from Java's built-in java.util toolbox.
import java.util.ArrayList;
import java.util.Scanner;

/**
 * LedgerApp
 * Starting point of the app. Holds the data every screen shares,
 * shows my cafe's storefront, then opens the home screen.
 */

public class LedgerApp {

    // One Scanner for the whole app, so screens never compete for keyboard input.
    // public + static: one shared copy that classes in other packages can use.
    public static Scanner scanner = new Scanner(System.in);

    // Every transaction, loaded from transactions.csv as soon as the app starts.
    public static ArrayList<Transaction> transactions = FileManager.loadTransactions();

    public static void main(String[] args) {

        // Picture first, so the screen isn't blank during the sound
        // Painted from cafe_storefront.txt
        CafeTheme.printStorefront();

        // Dreamy intro sound
        CafeSound.playDreamyWelcome();

        // App ends when the user exits the home screen
        HomeScreen.show();
    }
}