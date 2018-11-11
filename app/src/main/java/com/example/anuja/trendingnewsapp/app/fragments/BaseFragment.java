package com.example.anuja.trendingnewsapp.app.fragments;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.anuja.trendingnewsapp.common.ConnectionStatus;
import com.example.anuja.trendingnewsapp.model.ConnectionModel;
import com.example.anuja.trendingnewsapp.receiver.NetworkConnectivityReceiver;

public abstract class BaseFragment extends Fragment {

    // connection is available
    protected abstract void onConnected();

    // connection is unavailable
    protected abstract void onDisconnected();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleConnectivity();
    }

    /**
     * function called to handle the connectivity
     */
    private void handleConnectivity() {
        NetworkConnectivityReceiver connectivityReceiver = new NetworkConnectivityReceiver(getActivity().getApplicationContext());
        connectivityReceiver.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(@Nullable ConnectionModel connectionModel) {
                if(connectionModel.getConnectionStatus() == ConnectionStatus.CONNECTED)
                    onConnected();
                else if(connectionModel.getConnectionStatus() == ConnectionStatus.NOT_CONNECTED)
                    onDisconnected();
            }
        });
    }
}
