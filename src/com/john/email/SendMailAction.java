package com.john.email;

import java.io.IOException;
import java.util.ArrayList;

import com.jerry.bluetoothprinter.helper.ReceiptBuilder;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendMailAction {
	private String html,fromEmail,password,title;
	
	public SendMailAction(String html,String fromEmail,String password,String title){
		this.html = html;
		this.fromEmail = fromEmail;
		this.password = password;
		this.title = title;
	}
	
	public void sendEmail(ReceiptBuilder rb, String toEmail){
		StringBuilder sb = new StringBuilder("<pre>");
    	for (ArrayList<String> arr: rb.printout){
    		if (Integer.parseInt(arr.get(1)) == 3){
        		sb.append("<b>"+arr.get(0)+"</b><br>");
    		}else{
        		sb.append(arr.get(0)+"<br>");
    		}
//    		usbCtrl.sendByte(cmd, dev);                		
//    		usbCtrl.sendMsg(arr.get(0),"GBK", dev);
    		Log.d("Receipt Print", arr.get(0));              	
    	}
    	sb.append("</pre>");
//
//		try {
//			printImage("1234567",322,40);
//		} catch (WriterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		int index = html.indexOf("FILLTABLEHERE");
		if (index != -1){
			html = html.substring(0, index) + sb.toString() + html.substring(index + "FILLTABLEHERE".length());
		}
		AsyncTask<String, Void, String> sendMailTask = new SendMailTLSTask(); 
    	sendMailTask.execute(fromEmail,password,toEmail,title,html);
	}
}
