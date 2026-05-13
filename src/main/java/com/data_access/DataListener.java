package com.data_access;

import java.io.IOException;

/**
 * Defines a common interface for receiving raw data from an external source.
 */
public interface DataListener {

    /**
     * Listens for one raw data message from the external source.
     *
     * @return the received raw data message
     * @throws IOException if the data source cannot be read
     */
    RawDataMessage listen() throws IOException;

    /**
     * Closes the connection or resource used by the listener.
     *
     * @throws IOException if the resource cannot be closed
     */
    void close() throws IOException;
}