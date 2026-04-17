package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy { // filename must be the same as the class name

    private String BaseDirectory;

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /***
     * Constructor for FileOutputStrategy. It initializes the base directory where the output files will be stored.
     * @param baseDirectory the base directory for output files
     */
    public FileOutputStrategy(String baseDirectory) { // Constructor name must be the same as the class name

        this.BaseDirectory = baseDirectory;
    }

    /***
     * Outputs the generated patient data to a file. The method creates a file for each unique label and appends the data to the corresponding file.
     * 
     * @throws IOException if there is an error creating directories or writing to files
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(BaseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = file_map.computeIfAbsent(label, k -> Paths.get(BaseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}