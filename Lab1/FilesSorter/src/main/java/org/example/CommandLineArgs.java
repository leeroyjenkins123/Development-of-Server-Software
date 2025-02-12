package org.example;

import java.util.List;

public class CommandLineArgs {
    private String outputPath = ".";
    private String filePrefix = "";
    private boolean appendMode = false;
    private StatisticMode statType = StatisticMode.NONE;
    private List<String> inputFiles;

    public String getOutputPath(){ return outputPath;}
    public void setOutputPath(String outputPath){ this.outputPath = outputPath;}

    public String getFilePrefix(){ return filePrefix;}
    public void setFilePrefix(String filePrefix){ this.filePrefix = filePrefix;}

    public boolean getAppendMode(){ return appendMode;}
    public void setAppendMode(boolean appendMode){ this.appendMode = appendMode;}

    public StatisticMode getStatType(){ return statType;}
    public void setStatType(StatisticMode statType){ this.statType = statType;}

    public List<String> getInputFiles(){ return inputFiles;}
    public void setInputFiles(List<String> inputFiles){ this.inputFiles = inputFiles;}
}
