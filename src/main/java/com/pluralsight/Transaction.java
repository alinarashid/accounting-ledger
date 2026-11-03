package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    // Fields
    private LocalDate  date;
    LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // Constructor
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

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

//    @Override
//    public String toString() {
//        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        return String.format("%s | %s | %-25s | %-20s | %10.2f",
//                date, time.format(timeFmt), description, vendor, amount);
//    }

    @Override
    public String toString() {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String color = amount >= 0 ? CafeTheme.GREEN : CafeTheme.RED;

        return String.format("%s  %s  %-22s  %-20s  ", date, time.format(timeFmt), description, vendor)
                + color + String.format("%10.2f", amount) + CafeTheme.RESET;
    }
}
