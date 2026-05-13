# code converge - Note
The JaCoCo report shows 29% overall instruction coverage. The main Part 3 implementation packages are covered more strongly: `com.alerts` has 84% coverage 
and `com.data_management` has 60% coverage.

The tests focus on patient data storage, patient record retrieval, file-based data reading, and alert generation logic. These are the main requirements 
for Part 3.

Some packages have 0% coverage because they are simulator, networking, or design-support classes. These include `com.cardio_generator`, `com.
cardio_generator.generators`, `com.cardio_generator.outputs`, `com.data_access`, and `com.patient_identification`. They were not fully tested because they 
depend on simulator execution, TCP/WebSocket behavior, external files, or were mainly created for the UML/design part of the project.


