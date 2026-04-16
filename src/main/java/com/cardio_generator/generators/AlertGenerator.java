package com.cardio_generator.generators;
//imports sould follow the ASCII order

import com.cardio_generator.outputs.OutputStrategy;
import java.util.Random;

public class AlertGenerator implements PatientDataGenerator {

    public static final Random RANDOM_GENERATOR = new Random(); // static final fields should be in upper case with underscores (RANDOM_GENERATOR)
    private boolean[] alertStates; // false = resolved, true = pressed
                                   // fields name should be lowerCamelCase, not start with uppercase. (alertStates)

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                                     // fields name should be lowerCamelCase, not start with uppercase. (lambda)
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
