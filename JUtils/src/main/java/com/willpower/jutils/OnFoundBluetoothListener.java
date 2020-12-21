package com.willpower.jutils;

import android.bluetooth.BluetoothDevice;

public interface OnFoundBluetoothListener {
    void onFoundBluetooth(BluetoothDevice device);
    void onScanComplete();
}
