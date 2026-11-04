package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * FileManager
 * Handles everything to do with transactions.csv:
 * loading all transactions when the app starts, and saving new ones.
 */
public class FileManager {

    // Reads transactions.csv and turns every line into a Transaction object
    // e.g. "2026-11-02|08:05:00|Coffee beans 20lb|Bean Roasters|-320.00" -> one Transaction
    public static ArrayList<Transaction> loadTransactions() {
        // ? Why outside the try? So the return at the bottom can see it,
        //   and if the file fails to load we still return an empty list instead of crashing
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("transactions.csv"));

            // ! Skip the header line (date|time|description|vendor|amount), or parsing "date" would crash
            reader.readLine();
            String line;

            // * Keeps reading until readLine() returns null (the end of the file)
            while ((line = reader.readLine()) != null) {
                // ! "\\|" because | is a special character in split, the \\ means "a real pipe"
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
            // Runs if the file is missing or a line has bad data
            System.out.println(e.getMessage());
        }

        return transactions;
    }

    // Adds ONE new transaction to the end of transactions.csv
    public static void saveTransaction(Transaction transaction) {
        try {
            // * true = append mode: add to the end of the file instead of wiping it
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));

            // ! Without this, 09:00:00 would be saved as 09:00 (LocalTime drops :00 seconds)
            DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Builds the line in the exact CSV format, e.g. 2026-11-03|14:30:05|Tip|Counter Sales|12.00
            // ! %n at the START puts the new row on its own line (the file doesn't end with a blank line)
            // ! No spaces or $ in the file, or loadTransactions() couldn't parse it back
            String row = String.format("%n%s|%s|%s|%s|%.2f",
                    transaction.getDate(),
                    transaction.getTime().format(timeFmt),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());

            writer.write(row);

            // ! Closing the writer is what actually saves the data to the file
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}