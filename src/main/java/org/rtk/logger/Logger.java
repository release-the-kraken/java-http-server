package org.rtk.logger;

public class Logger {
    public void info(String input){
        System.out.println("INFO - ".concat(input));
    }

    public void error(String input){
        System.out.println("ERROR - ".concat(input));
    }
}
