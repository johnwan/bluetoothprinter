package com.jerry.bluetoothprinter.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.jerry.bluetoothprinter.helper.BarcodeUtil;
import com.jerry.bluetoothprinter.helper.PrinterCommands;
import com.jerry.bluetoothprinter.helper.ReceiptBuilder;
import com.jerry.bluetoothprinter.service.PrintDataService;
import com.jerry.bluetoothprinter.view.R;
import com.john.email.SendMailAction;

public class PrintDataAction implements OnClickListener {    
    private Context context = null;    
    private TextView deviceName = null;    
    private TextView connectState = null;    
    private EditText printData = null;    
    private String deviceAddress = null;    
    private PrintDataService printDataService = null;    
    
    public PrintDataAction(Context context, String deviceAddress,    
            TextView deviceName, TextView connectState, EditText printData) {    
        super();    
        this.context = context;    
        this.deviceAddress = deviceAddress;    
        this.deviceName = deviceName;    
        this.connectState = connectState;
        this.printData = printData;
        this.printDataService = new PrintDataService(this.context,    
                this.deviceAddress);    
        this.initView();    
    
    }    
    
    private void initView() {
    	printData = (EditText) ((Activity)context).findViewById(R.id.print_data);
        // 设置当前设备名称    
        this.deviceName.setText(this.printDataService.getDeviceName());    
        // 一上来就先连接蓝牙设备    
        boolean flag = this.printDataService.connect();    
        if (flag == false) {    
            // 连接失败    
            this.connectState.setText("Connect failed!");    
        } else {    
            // 连接成功    
            this.connectState.setText("Connect successed!");    
    
        }    
    }    
    
    public void setPrintData(EditText printData) {    
        this.printData = printData;    
    }    
    
    @Override    
    public void onClick(View v) {
//   	 Printing Text
	   	Date date = new Date();
	   	SimpleDateFormat dateformat = new SimpleDateFormat(
	   			"MM/DD/yyyy HH:mm:ss");
	   	String dateStr = dateformat.format(date);
	   	 ArrayList<Map<String,Object>> productList = new  ArrayList<Map<String,Object>>();
	   	 HashMap<String,Object> map = new HashMap<String,Object>();
	   	 map.put("qty", 12.00);
	   	 map.put("name", "Candy");
	   	 map.put("price", "32.19");
	   	 productList.add(map);
    	ReceiptBuilder rb = new ReceiptBuilder(0,"Internation POS", 12, dateStr, "12321", 15, productList, 32.19, 5, 37.19);
        if (v.getId() == R.id.printsample) {  
        	Log.d("sample", "sample");
//            String sendData = this.printData.getText().toString();
//            this.printDataService.send("123456789012345678901234567890123456789012345678901234567890" + "\n");

        	 // Sheet Type: 0 = small, 1 = large;
        	
        	for (ArrayList<String> arr: rb.printout){
            	this.printDataService.sendwithCommand(arr.get(0), Integer.parseInt(arr.get(1)));
        		Log.d("Receipt Print", arr.get(0));
        	}
        	 //Printing Barcode
        	try {
				this.printDataService.sendBitMapParam("I-9999999", 482, 50);// 322 40 for small // 482 70 for large // CAN PRINT AS LONG AS 32 BYTES!
				this.printDataService.sendFeedCutCommand();
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "Can't Print BitMap", Toast.LENGTH_SHORT).show();
			}

        }
        else if (v.getId() == R.id.send){
        	Log.d("send", "send");
//        	byte[] firstline = {0x10,0x14};
//        	this.printDataService.sendByteArray(firstline);
        	String sendData = this.printData.getText().toString();
//        	System.out.println(sendData);
        	try {
				this.printDataService.sendDataWithDelay(sendData);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if (v.getId() == R.id.delete){
//        	char del = 127;
//        	this.printDataService.send(Character.toString(del));
        	byte[] commmand = {0x55, 0x55, 0x32, 0x35, 0x38, 0x0D, 0x64,0x02,0x01,0x02,0x05,0x61,0x00,0x64,0x01};
        	try {
				this.printDataService.sendCommandWithDelay(commmand);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//        	String sendData = "\\x10\\x14"+this.printData.getText().toString();
//        	this.printDataService.send(sendData);
        }
        else if (v.getId() == R.id.sendemail){
        	// send email
        	String html = "";
			try {
				html = convertStreamToString(context.getAssets().open("index.html"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	SendMailAction sma = new SendMailAction(html,"jiaolong423@gmail.com","Jiaolong880423","Thank you for your business");
        	sma.sendEmail(rb,"jiaolong423@gmail.com");
        	Toast.makeText(context, "Mail sent successed!", Toast.LENGTH_SHORT).show();
        	
        }
//        else if (v.getId() == R.id.command) {    
//            this.printDataService.selectCommand();    
//    
//        }    
    }
	protected static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line+"\n");
		}
		is.close();
		return sb.toString();
	}
}