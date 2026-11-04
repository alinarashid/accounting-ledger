package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction
 * The blueprint for ONE transaction: one line in transactions.csv.
 * Positive amount = deposit (money in), negative amount = payment (money out).
 */
public class Transaction {

    // * private = only this class can touch the fields directly (encapsulation)
    //   other classes read them through the getters below
    private LocalDate date;            // 2026-11-02
    private LocalTime time;            // 08:05:00
    private String description;        // Coffee beans 20lb
    private String vendor;             // Bean Roasters
    private double amount;             // -320.00

    // Builds a Transaction from all 5 values, e.g. new Transaction(date, time, "Tip", "Counter Sales", 12.00)
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        // ? Why "this."? The parameters have the same names as the fields,
        //   so this.date means "the field", and date means "the value passed in"
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters: the public "door" other classes use to read the private fields
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // * How a transaction looks on screen. println(transaction) calls this automatically
    // e.g. 2026-11-02 | 08:05:00 | Coffee beans 20lb         | Bean Roasters        |    -320.00
    @Override
    public String toString() {
        // ! Without this, 09:00:00 would print as 09:00 (LocalTime drops :00 seconds)
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

        // %-25s / %-20s = fixed widths so the columns line up
        // %10.2f = always 2 decimals, lined up on the right
        return String.format("%s | %s | %-25s | %-20s | %10.2f",
                date, time.format(timeFmt), description, vendor, amount);
    }
}