package com.pluralsight;

import java.util.ArrayList;

public class LedgerApp {
    public static void main(String[] args) {

        ArrayList<Transaction> transactions = FileManager.loadTransactions();

        for ( Transaction transaction : transactions) {
            System.out.println(transaction);
        }

    }
}
