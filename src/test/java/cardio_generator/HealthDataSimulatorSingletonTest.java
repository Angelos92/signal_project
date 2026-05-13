package cardio_generator;

import static org.junit.jupiter.api.Assertions.*;

import com.cardio_generator.HealthDataSimulator;

import org.junit.jupiter.api.Test;

class HealthDataSimulatorSingletonTest {

    @Test
    void testHealthDataSimulatorReturnsSameInstance() {
        HealthDataSimulator firstInstance = HealthDataSimulator.getInstance();
        HealthDataSimulator secondInstance = HealthDataSimulator.getInstance();

        assertSame(firstInstance, secondInstance);
    }
}