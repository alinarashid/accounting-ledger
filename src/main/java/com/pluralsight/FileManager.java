package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileManager {

    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("transactions.csv"));
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                LocalDate date = LocalDate.parse(data[0]);
                LocalTime time = LocalTime.parse(data[1]);
                String description = data[2];
                String vendor = data[3];
                double amount = Double.parseDouble(data[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);
            }

            reader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return transactions;
    }

    public static void saveTransactions(Transaction transactions) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv",true));

            DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

            String row = String.format("%n%s|%s|%s|%s|%.2f",
                    transactions.getDate(),
                    transactions.getTime().format(timeFmt),
                    transactions.getDescription(),
                    transactions.getVendor(),
                    transactions.getAmount());

            writer.write(row);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }


}