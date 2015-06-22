package com.jerry.bluetoothprinter.view;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.jerry.bluetoothprinter.action.PrintDataAction;
import com.jerry.bluetoothprinter.helper.BarcodeUtil;
import com.jerry.bluetoothprinter.service.PrintDataService;

public class PrintDataActivity extends Activity {    
    private Context context = null;    
    
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);    
        this.setTitle("Bluetooth Connect");    
        this.setContentView(R.layout.printdata_layout);    
        this.context = this;    
        this.initListener();    
    }    
    
    /**  
     * 获得从上一个Activity传来的蓝牙地址  
     * Get Bluetooth Mac Address from last Activity
     * @return String  
     */    
    private String getDeviceAddress() {    
        // 直接通过Context类的getIntent()即可获取Intent    
        Intent intent = this.getIntent();    
        // 判断    
        if (intent != null) {    
            return intent.getStringExtra("deviceAddress");    
        } else {    
            return null;    
        }    
    }    
    
    private void initListener() {    
        TextView deviceName = (TextView) this.findViewById(R.id.device_name);    
        TextView connectState = (TextView) this    
                .findViewById(R.id.connect_state);    
    

        EditText printData = (EditText) this.findViewById(R.id.print_data);
//        EditText printData = new EditText(context);
        PrintDataAction printDataAction = new PrintDataAction(this.context,    
                this.getDeviceAddress(), deviceName, connectState, printData);    
    
        //ImageView image = (ImageView) this.findViewById(R.id.imageView);
        Button printsample = (Button) this.findViewById(R.id.printsample);
        Button send = (Button) this.findViewById(R.id.send);    
        Button del = (Button) this.findViewById(R.id.delete);  
        //Button command = (Button) this.findViewById(R.id.command);    
        Button sendEmail = (Button) this.findViewById(R.id.sendemail);    
        printDataAction.setPrintData(printData);
//        try {
//			image.setImageBitmap(BarcodeUtil.writeCode128("1234567", 80, 30));
//		} catch (WriterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        printsample.setOnClickListener(printDataAction);
        send.setOnClickListener(printDataAction);
        del.setOnClickListener(printDataAction);
        sendEmail.setOnClickListener(printDataAction);  
        printsample.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
    }    
    
        
    @Override    
    protected void onDestroy() {
        PrintDataService.disconnect();    
        super.onDestroy();    
    }    
        
}  