
# UML diagram - Description

## Alert Generation System

The Alert Generation System is designed to separate alert detection from alert dispatching. AlertGenerator is responsible for evaluating 
patient data and deciding whether an alert should be created. It checks PatientRecord objects, which contain the patient ID, record type, 
measurement value, and timestamp. If a value exceeds a critical threshold, such as a high heart rate or low oxygen saturation, AlertGenerator 
creates an Alert object.

The Alert class is kept simple because it only represents alert information: the patient ID, the condition, and the timestamp. It does not 
contain detection or routing logic. AlertManager is responsible for dispatching alerts, which keeps the system easier to extend later. For 
example, alert routing could later be changed to notify nurses, doctors, or external systems without changing the threshold-checking logic.

DataStorage, Patient, and PatientRecord are included as supporting classes because alerts are based on stored patient measurements. The 
design follows separation of concerns: storage classes store data, AlertGenerator evaluates data, and AlertManager handles alert routing.

## Data Storage System

The Data Storage System is designed to store incoming patient measurements securely and make them available for later retrieval and analysis. 
DataStorage is the central storage class. It organizes patients using a map, where each patient ID points to a Patient object. Each Patient 
owns multiple PatientRecord objects, and each record represents one timestamped measurement, such as heart rate, blood pressure, or oxygen 
saturation.

DataRetriever handles access to stored records instead of allowing other parts of the system to directly query DataStorage. It makes the 
design more secure and easier to maintain. Before returning patient records, it uses AccessController to check whether the requester has 
permission. It also uses AuditLog to record access attempts, which supports traceability and privacy requirements.

DeletionPolicy is included to represent data retention rules, such as removing records older than a certain number of days. DataReader is 
modeled as an interface so different input sources can load data into storage without changing DataStorage. Finally, it keeps the system 
flexible and supports future extensions.

## Patient Identification System
The Patient Identification System is designed to ensure that incoming simulator data is linked to the correct real hospital patient. 
PatientIdentifier performs the main matching logic by comparing simulator patient IDs against PatientIDMapping objects. Each mapping connects 
a simulator ID to a hospital patient ID, which can then be used to retrieve the corresponding HospitalPatient record.

HospitalPatient represents the real patient information stored in the hospital system, including identity details and medical history. 
IdentityManager coordinates the identification process and acts as the main entry point for the subsystem. If no matching patient is found, 
it delegates the problem to IdentityMismatchHandler, which creates and stores an IdentityAnomaly. It keeps error handling separate from 
matching logic and makes the system easier to maintain.

The design separates matching, patient information, and anomaly handling into different classes, which supports modularity and clear 
responsibility.
## Data Access / Side Simulator System
The Data Access Layer is designed to isolate the CHMS from the way external data arrives. The DataListener interface defines a common 
contract for receiving raw data, while TCPDataListener, WebSocketDataListener, and FileDataListener provide source-specific implementations. 
It allows the system to support different input methods without changing the rest of the application.

RawDataMessage represents the unprocessed data received from an external source. DataParser validates and converts that raw input into a 
standardized PatientRecord object. SO, it keeps parsing logic separate from network or file-reading logic. DataSourceAdapter coordinates the 
process by using a listener to retrieve raw data, using the parser to convert it, and then storing the result in DataStorage.

The design supports flexibility and low coupling. New data sources can be added by creating another DataListener implementation, while the 
storage and alert systems remain unchanged.
