package org.example;

import java.util.List;
import java.util.ArrayList;

public class CommandLineParser {
    public static CommandLineArgs parse(String[] args){
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        List<String> inputFiles = new ArrayList<>();

        for(int i=0;i<args.length;i++){
            String arg = args[i];
            switch (arg){
                case "-o":
                    commandLineArgs.setOutputPath(args[++i]);
                    break;
                case "-p":
                    commandLineArgs.setFilePrefix(args[++i]);
                    break;
                case "-a":
                    commandLineArgs.setAppendMode(true);
                    break;
                case "-s":
                    if(commandLineArgs.getStatType() == StatisticMode.FULL){
                        throw new IllegalArgumentException("Cannot use both -s and -f command");
                    }
                    commandLineArgs.setStatType(StatisticMode.SHORT);
                    break;
                case "-f":
                    if(commandLineArgs.getStatType() == StatisticMode.SHORT){
                        throw new IllegalArgumentException("Cannot use both -s and -f command");
                    }
                    commandLineArgs.setStatType(StatisticMode.FULL);
                    break;
                default:
                    if(arg.startsWith("-")){
                        throw new IllegalArgumentException("Unknown option: " + arg);
                    }
                    inputFiles.add(arg);
                    break;
            }
        }
        if(inputFiles.isEmpty()){
            throw new IllegalArgumentException("No input files specified");
        }

        commandLineArgs.setInputFiles(inputFiles);
        return commandLineArgs;
    }
}
