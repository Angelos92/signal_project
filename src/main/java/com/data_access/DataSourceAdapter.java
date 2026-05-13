package com.data_access;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.IOException;

/**
 * Connects external data sources to the internal data storage system.
 */
public class DataSourceAdapter {
    private DataListener dataListener;
    private DataParser dataParser;
    private DataStorage dataStorage;

    public DataSourceAdapter(
            DataListener dataListener,
            DataParser dataParser,
            DataStorage dataStorage
    ) {
        this.dataListener = dataListener;
        this.dataParser = dataParser;
        this.dataStorage = dataStorage;
    }

    /**
     * Collects one raw data message, parses it, and stores it in DataStorage.
     *
     * @throws IOException if the listener cannot read data
     */
    public void collectAndStoreData() throws IOException {
        RawDataMessage rawDataMessage = dataListener.listen();

        if (rawDataMessage == null) {
            return;
        }

        PatientRecord record = dataParser.parse(rawDataMessage);

        dataStorage.addPatientData(
                record.getPatientId(),
                record.getMeasurementValue(),
                record.getRecordType(),
                record.getTimestamp()
        );
    }
}