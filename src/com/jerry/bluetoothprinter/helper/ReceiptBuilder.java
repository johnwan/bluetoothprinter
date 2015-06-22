package com.jerry.bluetoothprinter.helper;

import java.util.ArrayList;
import java.util.Map;

public class ReceiptBuilder {
	protected static String[] SPLIT = {"================================","================================================"}; 
	protected static String[] SPACE = {"                                ","                                                "}; 
	private static String[] ITEMFORMAT = {"%-6.2f%-19s%-7s","%-6.2f%-35s%-7s"};
	private static String[] SUBFORMAT = {"%-25s%-7s","%-41s%-7s"};
	private static String[] TITELFORMAT = {"%-32s","%-48s"};
	private String storeName;
	private String url = "www.internationalpointofsale.com";
	private String phoneNum = "866-468-5767";
	private String invoiceNum = "INVOICE# ";
	private String datetime = "DATE/TIME: ";
	private String cashier = "CASHIER: ";
	private String station = "STATION: ";
	private ArrayList<Map<String,Object>> productList;
	protected String itemCount = "Item Count: ";
	private String subtotal = "Subtotal";
	private String tax1 = "Tax1";
	private String grandTotal = "GRAND TOTAL";
	public ArrayList<ArrayList<String>> printout;
	private static int NORMAL = 4;
	private static int LARGE = 3;
	
	
	public ReceiptBuilder(int Sheettype, String storeName, int invoiceNum, String datetime, String cashier, int station, ArrayList<Map<String,Object>> productList, 
			double subtotal, double tax1, double grandtotal){
		printout = new ArrayList<ArrayList<String>>();
		printout.add(buildArray("",0));
		this.storeName = String.format(TITELFORMAT[Sheettype], storeName);
		printout.add(buildArray(this.storeName,LARGE));
		this.invoiceNum += invoiceNum;
		printout.add(buildArray(String.format(TITELFORMAT[Sheettype], this.invoiceNum),NORMAL));
		this.datetime += datetime;
		printout.add(buildArray(String.format(TITELFORMAT[Sheettype], this.datetime),NORMAL));
		this.cashier += cashier;
		printout.add(buildArray(String.format(TITELFORMAT[Sheettype], this.cashier),NORMAL));
		this.station += station;
		printout.add(buildArray(String.format(TITELFORMAT[Sheettype], this.station),NORMAL));
		this.itemCount += productList.size();
		printout.add(buildArray(String.format(TITELFORMAT[Sheettype], this.itemCount),NORMAL));
		printout.add(buildArray(SPLIT[Sheettype],NORMAL));
		for(Map<String,Object> map: productList){
			String item = String.format(ITEMFORMAT[Sheettype], map.get("qty"), map.get("name"),"$"+map.get("price"));
			printout.add(buildArray(item,NORMAL));
		}
		printout.add(buildArray(SPLIT[Sheettype],NORMAL));
		this.subtotal = String.format(SUBFORMAT[Sheettype], this.subtotal, "$"+subtotal);
		this.tax1 = String.format(SUBFORMAT[Sheettype], this.tax1, "$"+tax1);
		this.grandTotal = String.format(SUBFORMAT[Sheettype], this.grandTotal, "$"+grandtotal);
		printout.add(buildArray(this.subtotal,NORMAL));
		printout.add(buildArray(this.tax1,NORMAL));
		printout.add(buildArray(this.grandTotal,LARGE));
		printout.add(buildArray(SPACE[Sheettype],NORMAL));
		
	}
	
	private ArrayList<String> buildArray(String str, int which){
		ArrayList<String> list = new ArrayList<String>();
		list.add(str);
		list.add(which+"");		
		return list;		
	}

}
