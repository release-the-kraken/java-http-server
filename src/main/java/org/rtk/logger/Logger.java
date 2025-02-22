package org.rtk.logger;

public class Logger {
    public static void info(String input){
        System.out.println("INFO - ".concat(input));
    }

    public static void error(String input, Exception e){
        System.out.println("ERROR - ".concat(input).concat(" ").concat(e.getLocalizedMessage()));
    }
}
