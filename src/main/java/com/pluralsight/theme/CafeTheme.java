package com.pluralsight.theme;

// My own class from another package (the receipt prints a Transaction)
import com.pluralsight.Transaction;

// Java's built-in tools: reading the art file, time format, list
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * CafeTheme
 * Everything about how Alina's Cafe LOOKS in the terminal:
 * colors, menu boards, header bars, the receipt, the storefront and the goodbye screen.
 * The screens call these methods, so every screen shares the same style.
 * */
public class CafeTheme {

    // ===== Colors =====
    // An ANSI color code is just a special String. Printing it changes the text color
    // until RESET is printed.
    public static final String RESET = "\u001B[0m";
    // These are medium shades on purpose: readable on BOTH light and dark consoles.
    public static final String PINK = rgb(212, 83, 126);
    public static final String LIGHT_PINK = rgb(212, 83, 126);
    public static final String BROWN = rgb(186, 117, 23);
    public static final String SKY = rgb(55, 138, 221);
    public static final String GREEN = rgb(99, 153, 34);
    public static final String RED = rgb(226, 75, 74);
    public static final String GRAY = rgb(136, 135, 128);
    public static final String YELLOW = rgb(186, 117, 23);

    // pink background + dark text, used for header bars
    private static final String BAR = bg(226, 88, 143) + rgb(42, 16, 32);

    // width of the menu boards and receipt (inside the borders)
    private static final int BOARD_WIDTH = 44;
    private static final int RECEIPT_WIDTH = 35;

    // builds a TEXT color code from red, green, blue numbers (0-255)
    public static String rgb(int r, int g, int b) {
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
    }

    // builds a BACKGROUND color code from red, green, blue numbers (0-255)
    public static String bg(int r, int g, int b) {
        return "\u001B[48;2;" + r + ";" + g + ";" + b + "m";
    }

    // ===== Header bar =====
    // A full-width pink bar with a title, e.g. "LEDGER  ·  ALL ENTRIES"
    public static void printHeaderBar(String title) {
        System.out.println();
        System.out.println(BAR + String.format("  %-76s", title) + RESET);
        System.out.println();
    }

    // ===== Menu board =====
    // A rounded box with a title and one line per option.
    // hints can be null if the menu doesn't need them.
    public static void printBoard(String title, String[] keys, String[] labels, String[] hints) {
        String top = " " + title + " ";
        int left = (BOARD_WIDTH - top.length()) / 2;
        int right = BOARD_WIDTH - top.length() - left;

        System.out.println();
        System.out.println(PINK + "  ╭" + "─".repeat(left) + BROWN + top + PINK + "─".repeat(right) + "╮" + RESET);

        for (int i = 0; i < keys.length; i++) {
            String start = "   " + keys[i] + "   " + labels[i];
            String line;

            if (hints != null) {
                // dots fill the gap between the label and the hint, like a cafe menu
                int dots = BOARD_WIDTH - start.length() - hints[i].length() - 4;
                line = "   " + LIGHT_PINK + keys[i] + RESET + "   " + labels[i] + " "
                        + GRAY + ".".repeat(Math.max(dots, 1)) + RESET + " " + hints[i] + "  ";
            } else {
                line = "   " + LIGHT_PINK + keys[i] + RESET + "   " + labels[i]
                        + " ".repeat(BOARD_WIDTH - start.length());
            }

            System.out.println(PINK + "  │" + RESET + line + PINK + "│" + RESET);
        }

        System.out.println(PINK + "  ╰" + "─".repeat(BOARD_WIDTH) + "╯" + RESET);
    }

    // The prompt under every menu
    public static void printPrompt() {
        System.out.print(SKY + "  Choose an option > " + RESET);
    }

    // ===== Table header =====
    // Column titles. The widths (22, 20, 10) must match Transaction's toString(), or the columns won't line up
    public static void printTableHeader() {
        System.out.println(LIGHT_PINK + String.format("%-10s  %-8s  %-22s  %-20s  %10s",
                "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT") + RESET);
        System.out.println(PINK + "─".repeat(78) + RESET);
    }

    // ===== Receipt =====
    // A pink receipt shown after a deposit or payment is saved
    public static void printReceipt(Transaction transaction) {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String border = "-".repeat(RECEIPT_WIDTH);

        String amountColor = transaction.getAmount() >= 0 ? GREEN : RED;
        String sign = transaction.getAmount() >= 0 ? "+ $" : "- $";
        String amountText = sign + String.format("%.2f", Math.abs(transaction.getAmount()));

        System.out.println();
        System.out.println(LIGHT_PINK + "   ." + border + "." + RESET);
        receiptLine(PINK + center("ALINA'S CAFE", RECEIPT_WIDTH) + RESET);
        receiptLine(GRAY + String.format(" %-17s%16s ",
                transaction.getDate(), transaction.getTime().format(timeFmt)) + RESET);
        receiptLine(LIGHT_PINK + border + RESET);
        receiptLine(String.format(" %-34s", cut(transaction.getDescription(), 33)));
        receiptLine(" " + GRAY + String.format("%-21s", cut(transaction.getVendor(), 20)) + RESET
                + amountColor + String.format("%12s", amountText) + RESET + " ");
        receiptLine(LIGHT_PINK + border + RESET);
        receiptLine(GREEN + center("saved to the books", RECEIPT_WIDTH) + RESET);
        System.out.println(LIGHT_PINK + "   '" + border + "'" + RESET);
    }

    // prints one line of the receipt between the pink side borders
    private static void receiptLine(String content) {
        System.out.println(LIGHT_PINK + "   :" + RESET + content + LIGHT_PINK + ":" + RESET);
    }

    // puts text in the middle of a given width
    private static String center(String text, int width) {
        int left = (width - text.length()) / 2;
        int right = width - text.length() - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }

    // shortens text that is too long so it doesn't break the receipt border
    private static String cut(String text, int max) {
        if (text.length() > max) {
            return text.substring(0, max);
        }
        return text;
    }

    // ===== Goodbye screen =====
    public static void printGoodbye() {
        System.out.println();
        System.out.println(GRAY + "              (  )  (" + RESET);
        System.out.println(GRAY + "               )  (  )" + RESET);
        System.out.println(BROWN + "            .-----------." + RESET);
        System.out.println(BROWN + "            |  " + LIGHT_PINK + "♥" + YELLOW + "  *  " + LIGHT_PINK + "♥" + BROWN + "  |__" + RESET);
        System.out.println(BROWN + "            |  " + YELLOW + "*" + LIGHT_PINK + "  ♥  " + YELLOW + "*" + BROWN + "  |  |" + RESET);
        System.out.println(BROWN + "            |           |__/" + RESET);
        System.out.println(BROWN + "             \\_________/" + RESET);
        System.out.println(GRAY + "          ~~~~~~~~~~~~~~~~~~~" + RESET);
        System.out.println();
        System.out.println(PINK + "     Thanks for visiting Alina's Cafe!" + RESET);
        System.out.println(SKY + "          see you tomorrow" + RESET);
        System.out.println();
    }

    // ===== Storefront (pixel art) =====
    // Reads cafe_storefront.txt (one letter = one pixel) and paints it.
    // Two pixel rows are printed per terminal line using the half-block character:
    // the top pixel is the text color, the bottom pixel is the background color.
    public static void printStorefront() {
        ArrayList<String> rows = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("cafe_storefront.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                // skip the comment lines and the blank line at the top of the file
                if (line.startsWith("#") || line.isBlank()) {
                    continue;
                }
                rows.add(line);
            }
            reader.close();

        } catch (Exception e) {
            // if the art file is missing, show a simple title instead of crashing
            System.out.println(PINK + "\n  ===== ALINA'S CAFE =====\n" + RESET);
            return;
        }

        System.out.println();

        // go down the picture two rows at a time
        for (int y = 0; y < rows.size(); y += 2) {
            String topRow = rows.get(y);
            String bottomRow = (y + 1 < rows.size()) ? rows.get(y + 1) : "";

            StringBuilder out = new StringBuilder("  ");

            for (int x = 0; x < topRow.length(); x++) {
                int[] top = colorFor(topRow.charAt(x));
                int[] bottom = (x < bottomRow.length()) ? colorFor(bottomRow.charAt(x)) : null;

                if (top == null && bottom == null) {
                    out.append(RESET).append(" ");                                    // both empty
                } else if (bottom == null) {
                    out.append(RESET).append(rgb(top[0], top[1], top[2])).append("▀");  // only top
                } else if (top == null) {
                    out.append(RESET).append(rgb(bottom[0], bottom[1], bottom[2])).append("▄"); // only bottom
                } else {
                    out.append(rgb(top[0], top[1], top[2]))
                            .append(bg(bottom[0], bottom[1], bottom[2]))
                            .append("▀");                                                    // both colored
                }
            }

            out.append(RESET);
            System.out.println(out);
        }

        System.out.println();
    }

    // turns one letter from the art file into its red, green, blue color
    // returns null for '.' (empty pixel)
    private static int[] colorFor(char c) {
        return switch (c) {
            case 'r' -> new int[]{122, 68, 40};    // roof
            case 'h' -> new int[]{156, 74, 54};    // chimney
            case 'b' -> new int[]{232, 106, 154};  // sign border
            case 's' -> new int[]{168, 220, 235};  // sign
            case 't' -> new int[]{74, 31, 56};     // sign text
            case 'a' -> new int[]{226, 88, 143};   // awning pink
            case 'w' -> new int[]{255, 244, 236};  // awning cream
            case 'p' -> new int[]{245, 182, 201};  // wall
            case 'P' -> new int[]{227, 154, 176};  // wall shadow
            case 'd' -> new int[]{110, 65, 40};    // door
            case 'D' -> new int[]{185, 130, 88};   // door panel
            case 'k' -> new int[]{245, 197, 66};   // door knob
            case 'f' -> new int[]{107, 58, 34};    // window frame
            case 'g' -> new int[]{255, 240, 210};  // glass
            case 'G' -> new int[]{255, 248, 232};  // glass glare
            case 'l' -> new int[]{192, 138, 90};   // shelf
            case 'c' -> new int[]{90, 52, 32};     // chocolate
            case 'C' -> new int[]{138, 86, 52};    // chocolate light
            case 'm' -> new int[]{255, 255, 255};  // cream
            case 'x' -> new int[]{224, 72, 72};    // red
            case 'y' -> new int[]{243, 216, 101};  // yellow
            case 'n' -> new int[]{242, 155, 180};  // pink cake
            case 'u' -> new int[]{142, 91, 208};   // purple
            case 'v' -> new int[]{168, 121, 224};  // lavender
            case 'O' -> new int[]{240, 153, 123};  // coral
            case 'o' -> new int[]{122, 74, 46};    // flower box
            case 'L' -> new int[]{255, 214, 107};  // lamp light
            case 'j' -> new int[]{107, 58, 34};    // lamp top
            case 'e' -> new int[]{109, 190, 90};   // leaf
            case 'E' -> new int[]{63, 143, 58};    // leaf dark
            case 'q' -> new int[]{184, 90, 48};    // pot
            case 'Q' -> new int[]{143, 67, 34};    // pot rim
            case 'i' -> new int[]{207, 204, 219};  // sidewalk
            case 'I' -> new int[]{169, 166, 188};  // sidewalk dark
            default -> null;                       // '.' = empty
        };
    }
}