package MidTermProject.Methods;

import java.util.Scanner;

import static MidTermProject.Methods.Colors.*;

public class KeyboardInput {
    private final Scanner scanner;

    public KeyboardInput() {
        scanner = new Scanner(System.in);
    }

    /**
     * @param promptMsg  Main message
     * @param errorMsg   Error message if input is not an integer
     * @param lowerLimit Lower limit for checking condition
     * @param upperLimit Upper limit for checking condition
     * @return Valid integer
     * @Description Keeps prompting the user until the introduced value is an integer between limits
     */
    public int readInteger(String promptMsg, String errorMsg, int lowerLimit, int upperLimit) {
        int num = 0;
        String strInput;
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            try {
                // Parsing string to int
                num = Integer.parseInt(strInput);
                if (num >= lowerLimit && num <= upperLimit) valid = true;
                else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
            } catch (NumberFormatException nfe) {
                System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
            }
        }
        return num;
    }

    /**
     * @param promptMsg Main message
     * @param errorMsg  Error message if input is not a double
     * @return Valid double
     * @Description Keeps prompting the user until the introduced value is a double (integer also valid)
     */
    public double readDouble(String promptMsg, String errorMsg) {
        double num = 0;
        String strInput;
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            try {
                // Parsing string to int
                num = Double.parseDouble(strInput);
                if (num >= 0) valid = true;
                else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
            } catch (NumberFormatException nfe) {
                System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
            }
        }
        return num;
    }

    /**
     * @param promptMsg Main message
     * @param errorMsg  Error message if input is not a non-empty, non-numeric string
     * @return Valid string
     * @Description Keeps prompting the user until the introduced value is a non-empty, non-numeric string
     */
    public String readString(String promptMsg, String errorMsg) {
        String strInput = "";
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            try {
                Integer.parseInt(strInput);
                System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
            } catch (NumberFormatException nfe) {
                if (strInput.length() > 0) valid = true;
                else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
            }
        }
        return strInput;
    }

    /**
     * @param promptMsg Main message
     * @param errorMsg  Error message if input is not a non-empty string with less than 60 characters
     * @return Valid string
     * @Description Keeps prompting the user until the introduced value is a non-empty string with less than 60 characters
     */
    public String readTitle(String promptMsg, String errorMsg) {
        String strInput = "";
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            // Could also be just a number, so we don't need to check it
            if (strInput.length() > 0 && strInput.length() <= 60) valid = true;
            else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
        }
        return strInput;
    }

    /**
     * @param promptMsg Main message
     * @param errorMsg  Error message if input is not a non-empty, non-numeric string with max 40 characters
     * @return Valid string
     * @Description Keeps prompting the user until the introduced value is a valid name
     */
    public String readName(String promptMsg, String errorMsg) {
        String strInput = "";
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            // It accepts Ñ!!
            if(strInput.matches("^([a-zA-Z]{2,}\\s?([a-zA-Z]{1,}'?-?[a-zA-Z]{2,})?\\s?([a-zA-Z]{1,})?)") && strInput.length() <= 40) valid = true;
            else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
        }
        return strInput;
    }

    /**
     * @param promptMsg Main message
     * @param errorMsg  Error message if input is not an email-like expression
     * @return Valid email
     * @Description Keeps prompting the user until the introduced value is a valid email
     */
    public String readEmail(String promptMsg, String errorMsg) {
        String strInput = "";
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            if (strInput.matches("^([a-zA-Z0-9_\\-]+)@([a-zA-Z0-9_\\-]+)\\.([a-zA-Z]{2,5})$")) valid = true;
            else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
        }
        return strInput;
    }

    /**
     * @param promptMsg Main message
     * @param errorMsg  Error message if input is not an USN-like expression
     * @return Valid ISBN
     * @Description Keeps prompting the user until the introduced value is a valid USN (10 alphanumeric characters)
     */
    public String readUsn(String promptMsg, String errorMsg) {
        String strInput = "";
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            if (strInput.matches("^[a-zA-Z0-9]{10}$")) valid = true;
            else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
        }
        return strInput;
    }

    /**
     * @param promptMsg Main message
     * @param errorMsg  Error message if input is not an ISBN-like expression
     * @return Valid ISBN
     * @Description Keeps prompting the user until the introduced value is a valid ISBN
     */
    public String readIsbn(String promptMsg, String errorMsg) {
        String strInput = "";
        boolean valid = false;

        System.out.print(promptMsg);

        while (!valid) {
            strInput = scanner.nextLine();
            // SOURCE (ISBN regex): https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s13.html
            if (strInput.matches("^(?:ISBN(?:-13)?:?\\ )?(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})[-\\ 0-9]{17}$)97[89][-\\ ]?[0-9]{1,5}[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$")) valid = true;
            else System.out.print(ANSI_RED + errorMsg + ANSI_RESET);
        }
        return strInput;
    }

    /*private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static String unCapitalize(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    *//**
     * @Description Splits string by ", " and prints the first element with a point at the end
     * @param msg Message to split
     *//*
    private static void splitMsg(String msg) {
        if (msg.contains(", ")) {
            System.out.println(ANSI_RED + msg.split(", ")[0] + ". " + ANSI_RESET);
            System.out.println();
        }
    }*/
}
