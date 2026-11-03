# Accounting Ledger

A Java command-line app for tracking financial transactions: every deposit coming in and every payment going out. All transactions are saved to a CSV file, so your history is still there the next time you open the app.

Built as Capstone 1 for the Year Up United Software pathway.

---

## Features

### Home screen
- **D) Add Deposit**: record money coming in (always saved as positive)
- **P) Make Payment**: record money going out (always saved as negative, even if you type a positive number)
- **L) Ledger**: view your transactions
- **X) Exit**

### Ledger screen
All lists show the **newest entries first**.
- **A) All**: every transaction
- **D) Deposits**: money in only
- **P) Payments**: money out only
- **R) Reports**: open the reports screen
- **H) Home**: back to the home screen

### Reports screen
- **1) Month To Date**: this month so far
- **2) Previous Month**: all of last month (works across years, e.g. January → December)
- **3) Year To Date**: this year so far
- **4) Previous Year**: all of last year
- **5) Search by Vendor**: case-insensitive, e.g. `bean roasters` matches `Bean Roasters`
- **0) Back**: back to the ledger screen

Every report prints a friendly message when nothing matches.

---

## How to run

**Requirements:** Java 17 and Maven

### Option 1: IntelliJ
1. Clone the repo and open the folder in IntelliJ
2. Open `src/main/java/com/pluralsight/LedgerApp.java`
3. Click ▶️ next to `main`

### Option 2: Terminal
From the project root (the folder with `pom.xml` and `transactions.csv`):

```bash
mvn compile
java -cp target/classes com.pluralsight.LedgerApp
```

> ⚠️ Always run from the project root. The app looks for `transactions.csv` in the folder it's started from.

---

## Data file

Transactions are stored in `transactions.csv`, one per line:

```
date|time|description|vendor|amount
2026-11-02|08:05:00|Coffee beans 20lb|Bean Roasters|-320.00
2026-11-01|18:10:00|Sunday sales|Counter Sales|1388.40
```

- Positive amount = deposit
- Negative amount = payment
- New transactions are **appended** to the end of the file, so existing history is never overwritten.

---

## Project structure

```
src/main/java/com/pluralsight/
├── Transaction.java   → one transaction (date, time, description, vendor, amount)
├── FileManager.java   → loads transactions from the CSV and saves new ones
└── LedgerApp.java     → main() plus all the screens, menus and reports
```

Each class has one job: `Transaction` holds the data, `FileManager` handles the file, and `LedgerApp` talks to the user.

---

## Code highlight: previous month without the January bug

A simple approach would use `month - 1` to find last month, but that breaks in January (month `0` doesn't exist, and the year needs to change too). Instead, the app lets `LocalDate` do the calendar math:

```java
LocalDate today = LocalDate.now();
LocalDate previousMonth = today.minusMonths(1);   // Jan 2027 → Dec 2026 ✅

int year = previousMonth.getYear();
int month = previousMonth.getMonthValue();
```

Then each transaction is kept only if **both** its year and month match, so November 2025 never sneaks into a November 2026 report.