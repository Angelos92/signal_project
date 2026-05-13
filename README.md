# Cardio Data Simulator

The Cardio Data Simulator is a Java-based application designed to simulate real-time cardiovascular data for multiple patients. This tool is particularly useful for educational purposes, enabling students to interact with real-time data streams of ECG, blood pressure, blood saturation, and other cardiovascular signals.

## Features

- Simulate real-time ECG, blood pressure, blood saturation, and blood levels data.
- Supports multiple output strategies:
  - Console output for direct observation.
  - File output for data persistence.
  - WebSocket and TCP output for networked data streaming.
- Configurable patient count and data generation rate.
- Randomized patient ID assignment for simulated data diversity.

## Getting Started

### Prerequisites

- Java JDK 11 or newer.
- Maven for managing dependencies and compiling the application.

### Installation

1. Clone the repository:

   ```sh
   git clone https://github.com/tpepels/signal_project.git
   ```

2. Navigate to the project directory:

   ```sh
   cd signal_project
   ```

3. Compile and package the application using Maven:
   ```sh
   mvn clean package
   ```
   This step compiles the source code and packages the application into an executable JAR file located in the `target/` directory.

### Running the Simulator

After packaging, you can run the simulator directly from the executable JAR:

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar
```

To run with specific options (e.g., to set the patient count and choose an output strategy):

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar --patient-count 100 --output file:./output
```

### Supported Output Options

- `console`: Directly prints the simulated data to the console.
- `file:<directory>`: Saves the simulated data to files within the specified directory.
- `websocket:<port>`: Streams the simulated data to WebSocket clients connected to the specified port.
- `tcp:<port>`: Streams the simulated data to TCP clients connected to the specified port.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Project members
- student ID: 6381457

# UML diagram - Description

## Alert Generation System

The Alert Generation System is designed to separate alert detection from alert dispatching. AlertGenerator is responsible for evaluating patient data and deciding whether an alert should be created. It checks PatientRecord objects, which contain the patient ID, record type, measurement value, and timestamp. If a value exceeds a critical threshold, such as a high heart rate or low oxygen saturation, AlertGenerator creates an Alert object.

The Alert class is kept simple because it only represents alert information: the patient ID, the condition, and the timestamp. It does not contain detection or routing logic. AlertManager is responsible for dispatching alerts, which keeps the system easier to extend later. For example, alert routing could later be changed to notify nurses, doctors, or external systems without changing the threshold-checking logic.

DataStorage, Patient, and PatientRecord are included as supporting classes because alerts are based on stored patient measurements. This design follows separation of concerns: storage classes store data, AlertGenerator evaluates data, and AlertManager handles alert routing.

## Data Storage System

The Data Storage System is designed to store incoming patient measurements securely and make them available for later retrieval and analysis. DataStorage is the central storage class. It organizes patients using a map, where each patient ID points to a Patient object. Each Patient owns multiple PatientRecord objects, and each record represents one timestamped measurement, such as heart rate, blood pressure, or oxygen saturation.

DataRetriever handles access to stored records instead of allowing other parts of the system to directly query DataStorage. This makes the design more secure and easier to maintain. Before returning patient records, it uses AccessController to check whether the requester has permission. It also uses AuditLog to record access attempts, which supports traceability and privacy requirements.

DeletionPolicy is included to represent data retention rules, such as removing records older than a certain number of days. DataReader is modeled as an interface so different input sources can load data into storage without changing DataStorage. This keeps the system flexible and supports future extensions.

## Patient Identification System

## Data Access / Side Simulator System