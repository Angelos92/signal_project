package com.cardio_generator.outputs;
/***
 * Interface for outputting generating patient data.
 */
public interface OutputStrategy {
    void output(int patientId, long timestamp, String label, String data);
}
