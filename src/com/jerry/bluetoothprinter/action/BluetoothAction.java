package com.jerry.bluetoothprinter.action;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jerry.bluetoothprinter.service.BluetoothService;
import com.jerry.bluetoothprinter.view.R;

public class BluetoothAction implements OnClickListener {    
    
    private Button switchBT = null;    
    private Button searchDevices = null;    
    private Activity activity = null;    
    
    private ListView unbondDevices = null;    
    private ListView bondDevices = null;    
    private Context context = null;    
    private BluetoothService bluetoothService = null;    
    
    public BluetoothAction(Context context, ListView unbondDevices,    
            ListView bondDevices, Button switchBT, Button searchDevices,    
            Activity activity) {    
        super();    
        this.context = context;    
        this.unbondDevices = unbondDevices;    
        this.bondDevices = bondDevices;    
        this.switchBT = switchBT;    
        this.searchDevices = searchDevices;    
        this.activity = activity;    
        this.bluetoothService = new BluetoothService(this.context,    
                this.unbondDevices, this.bondDevices, this.switchBT,    
                this.searchDevices);    
    }    
    
    public void setSwitchBT(Button switchBT) {    
        this.switchBT = switchBT;    
    }    
    
    public void setSearchDevices(Button searchDevices) {    
        this.searchDevices = searchDevices;    
    }    
    
    public void setUnbondDevices(ListView unbondDevices) {    
        this.unbondDevices = unbondDevices;    
    }    
    
    /**  
     * ��ʼ������  
     */    
    public void initView() {    
    
        if (this.bluetoothService.isOpen()) {    
            System.out.println("Bluetooth is On!");    
            switchBT.setText("Close Bluetooth");    
        }    
        if (!this.bluetoothService.isOpen()) {    
            System.out.println("Bluetooth is Off!");    
            this.searchDevices.setEnabled(false);    
        }    
    }    
    
    private void searchDevices() {    
        bluetoothService.searchDevices();    
    }    
    
    /**  
     * ���ְ�ť�ļ���  
     */    
    @Override    
    public void onClick(View v) {    
        if (v.getId() == R.id.searchDevices) {    
            this.searchDevices();    
        } else if (v.getId() == R.id.return_Bluetooth_btn) {    
            activity.finish();    
        } else if (v.getId() == R.id.openBluetooth_tb) {    
            if (!this.bluetoothService.isOpen()) {    
                // Bluetooth is off
                System.out.println("Bluetooth is off");    
                this.bluetoothService.openBluetooth(activity);    
            } else {    
                // Bluetooth is on
                System.out.println("Bluetooth is on");    
                this.bluetoothService.closeBluetooth();    
    
            }    
    
        }    
    }    
    
}