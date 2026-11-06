package com.pluralsight;

// Java's built-in tools for reading and writing files
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

// Java's built-in date, time and list tools
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * FileManager
 * Everything to do with transactions.csv:
 * loads all transactions when the app starts, and saves each new one.
 */
public class FileManager {

    // Reads transactions.csv and turns every line into a Transaction object
    // e.g. "2026-11-02|08:05:00|Coffee beans 20lb|Bean Roasters|-320.00" -> one Transaction
    public static ArrayList<Transaction> loadTransactions() {

        // Made outside the try, so if loading fails we still return an empty list (no crash)
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("transactions.csv"));

            // Skip the header line (date|time|description|vendor|amount), or parsing "date" would crash
            reader.readLine();
            String line;

            // Keep reading until readLine() returns null (the end of the file)
            while ((line = reader.readLine()) != null) {
                // "\\|" because | is a special character in split, the \\ means "a real pipe"
                String[] data = line.split("\\|");

                // Turn each piece of text into the right type
                LocalDate date = LocalDate.parse(data[0]);       // "2026-11-02" -> date
                LocalTime time = LocalTime.parse(data[1]);       // "08:05:00"   -> time
                String description = data[2];
                String vendor = data[3];
                double amount = Double.parseDouble(data[4]);     // "-320.00"    -> number

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);
            }

            reader.close();

        } catch (Exception e) {
            // File missing or a line has bad data.
            System.out.println(e.getMessage());
        }

        return transactions;
    }

    // Adds ONE new transaction to the end of transactions.csv
    public static void saveTransaction(Transaction transaction) {
        try {
            // true = append mode: add to the end of the file instead of wiping the file
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));

            // HH:mm:ss keeps the seconds (otherwise 09:00:00 saves as 09:00)
            DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Same format the loader reads: no spaces, no $
            // %n at the START puts the new row on its own line
            String row = String.format("%n%s|%s|%s|%s|%.2f",
                    transaction.getDate(),
                    transaction.getTime().format(timeFmt),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());

            writer.write(row);

            // Closing the writer is what actually saves the data to the file
            writer.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}