package org.example;

public class Main {
    public static void main(String[] args) {
        try{
            System.out.println("Hello, World!");
            CommandLineArgs commandLineArgs = CommandLineParser.parse(args);
            DataProcessor dataProcessor = new DataProcessor(commandLineArgs);
            dataProcessor.process();
        }
        catch (IllegalArgumentException e){
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}