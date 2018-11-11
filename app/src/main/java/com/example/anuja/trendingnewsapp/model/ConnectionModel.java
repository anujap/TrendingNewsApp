package com.example.anuja.trendingnewsapp.model;

import com.example.anuja.trendingnewsapp.common.ConnectionStatus;

public class ConnectionModel {

    /**
     * the signal strength
     */
    private ConnectionStatus connectionStatus;

    public ConnectionModel(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }
}
