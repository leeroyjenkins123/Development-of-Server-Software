package org.example;

import java.io.FileWriter;

public class FileOpener {
    public FileWriter[] GetFiles(String pathToFiles, int count){
        FileWriter[] fileWriters = new FileWriter[count];
        try{
            for(int i=0;i<count;i++){
                FileWriter file = new FileWriter(pathToFiles+(i+1) + ".txt", true);
                fileWriters[i] = file;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileWriters;
    }
}
