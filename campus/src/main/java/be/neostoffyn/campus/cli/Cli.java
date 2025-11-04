package be.neostoffyn.campus.cli;

import java.util.Scanner;

public final class Cli {
    private Cli() {}

    public static void success(String msg) { System.out.println("✅ " + msg); }
    public static void error(String msg) { System.out.println("❌ " + msg); }
    public static void error(Throwable ex) { error(ex.getMessage()); }


    public static int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static long readLong(Scanner scanner) {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}