package com.pluralsight;

// Java's built-in date and time tools
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Colors, from the theme package
import com.pluralsight.theme.CafeTheme;

/**
 * Transaction
 * One transaction = one line in transactions.csv.
 * Positive amount = deposit (money in), negative = payment (money out).
 */
public class Transaction {

    // private: only this class can change these. Other classes read them with the getters.
    private LocalDate date;            // 2026-11-02
    private LocalTime time;            // 08:05:00
    private String description;        // Coffee beans 20lb
    private String vendor;             // Bean Roasters
    private double amount;             // -320.00

    // Builds a Transaction from all 5 values, e.g. new Transaction(date, time, "Tip", "Counter Sales", 12.00)
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {

        // this.date = the field, date = the value passed in (same name, so "this." tells them apart)
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters: let other classes read the private fields.
    // No setters, so a transaction can't be changed after it's made.
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

    // How one transaction prints on screen. println(transaction) calls this automatically.
    @Override
    public String toString() {

        // HH:mm:ss keeps the seconds (otherwise 09:00:00 prints as 09:00)
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Short if/else: money in = green, money out = red
        String color = amount >= 0 ? CafeTheme.GREEN : CafeTheme.RED;

        // These widths must match CafeTheme.printTableHeader(), or the columns won't line up
        return String.format("%s  %s  %-22s  %-20s  ", date, time.format(timeFmt), description, vendor)
                + color + String.format("%10.2f", amount) + CafeTheme.RESET; // RESET = back to normal color
    }
}