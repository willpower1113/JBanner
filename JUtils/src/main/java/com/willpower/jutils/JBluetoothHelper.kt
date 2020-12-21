package com.willpower.jutils

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.support.annotation.RequiresPermission
import android.util.Log

class JBluetoothHelper(private val mContext: Context, private val bluetoothListener: OnFoundBluetoothListener) {
    private val mHandler = Handler(Looper.getMainLooper())
    private var mBluetoothAdapter: BluetoothAdapter = getDefaultAdapter()

    class FoundBluetoothReceiver(private val listener: OnFoundBluetoothListener) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when (intent.action) {
                ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d(JBuildConfig.TAG, "发现设备：${device?.name} : ${device?.address}")
                    listener.onFoundBluetooth(device)
                }
                ACTION_DISCOVERY_STARTED -> {
                    Log.d(JBuildConfig.TAG, "开始扫描")
                }
                ACTION_DISCOVERY_FINISHED -> {
                    Log.d(JBuildConfig.TAG, "扫描结束")
                    listener.onScanComplete()
                }
            }
        }
    }

    /**
     * 开启蓝牙
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun openBluetooth() {
        mBluetoothAdapter.enable()
    }

    /**
     * 关闭蓝牙
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun closeBluetooth() {
        mBluetoothAdapter.disable()
    }

    fun scanBluetooth() {
        mBluetoothAdapter.bondedDevices.forEach {
            Log.d(JBuildConfig.TAG, "已绑定设备：${it.name}: ${it.address}")
            bluetoothListener.onFoundBluetooth(it)
        }
        val filter = IntentFilter()
        filter.addAction(ACTION_FOUND)
        filter.addAction(ACTION_DISCOVERY_STARTED)
        filter.addAction(ACTION_DISCOVERY_FINISHED)
        mContext.registerReceiver(FoundBluetoothReceiver(bluetoothListener), filter)
        mBluetoothAdapter.startDiscovery()
    }

    fun scanBluetooth(delayClose: Long) {
        scanBluetooth()
        mHandler.postDelayed({ mBluetoothAdapter.cancelDiscovery() }, delayClose)
    }
}