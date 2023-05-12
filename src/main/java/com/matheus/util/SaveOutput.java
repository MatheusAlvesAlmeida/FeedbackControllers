package com.matheus.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveOutput {
    private static final String OUTPUTS_DIRECTORY = "./outputs/";

    public static void saveToFile(String filename, String string) throws IOException {
        String filePath = OUTPUTS_DIRECTORY + filename;
        File file = new File(filePath);

        boolean fileExists = file.exists();

        FileWriter writer = new FileWriter(file, true);
        if (!fileExists) {
            writer.write("QUEUE_SIZE,ARRIVAL_RATE,PC_COMPUTED\n");
        }

        writer.write(string);

        writer.close();
    }

    public static void saveBasicOnOffResult(double queueSize, double arrivalRate, int newPC) {
        try {
            String content = String.format("%.2f,%.2f,%d\n", queueSize, arrivalRate, newPC);
            saveToFile("basic_onoff.csv", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
