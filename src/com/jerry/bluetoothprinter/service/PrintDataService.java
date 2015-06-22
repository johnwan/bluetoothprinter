package com.jerry.bluetoothprinter.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.google.zxing.WriterException;
import com.jerry.bluetoothprinter.helper.BarcodeUtil;
import com.jerry.bluetoothprinter.helper.PrinterCommands;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

public class PrintDataService {
	private Context context = null;
	private String deviceAddress = null;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private BluetoothDevice device = null;
	private static BluetoothSocket bluetoothSocket = null;
	private static OutputStream outputStream = null;
	private static final UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private boolean isConnection = false;
	final String[] items = { "Reset Printer", "Standard ASCII", "Small ASCII", "Large Font","Standard Font",
			"Doubled", "Cancel Bold", "Bold", "Cancel Reverse", "Choose Reverse", "Cancel Invert", "Choose Invert",
			"Cancel Rotate 90��", "Choose Rotate 90��" };
	final byte[][] byteCommands = { { 0x1b, 0x40 },// ��λ��ӡ��
			{ 0x1b, 0x4d, 0x00 },// ��׼ASCII����
			{ 0x1b, 0x4d, 0x01 },// ѹ��ASCII����
			{ 0x1d, 0x21, 0x01 },// ����Ŵ�
			{ 0x1d, 0x21, 0x00 },// ���岻�Ŵ�
			{ 0x1d, 0x21, 0x11 },// ��߼ӱ�
			{ 0x1b, 0x45, 0x00 },// ȡ���Ӵ�ģʽ
			{ 0x1b, 0x45, 0x01 },// ѡ��Ӵ�ģʽ
			{ 0x1b, 0x7b, 0x00 },// ȡ�����ô�ӡ
			{ 0x1b, 0x7b, 0x01 },// ѡ���ô�ӡ
			{ 0x1d, 0x42, 0x00 },// ȡ���ڰ׷���
			{ 0x1d, 0x42, 0x01 },// ѡ��ڰ׷���
			{ 0x1b, 0x56, 0x00 },// ȡ��˳ʱ����ת90��
			{ 0x1b, 0x56, 0x01 },// ѡ��˳ʱ����ת90��
	};

	public PrintDataService(Context context, String deviceAddress) {
		super();
		this.context = context;
		this.deviceAddress = deviceAddress;
		this.device = this.bluetoothAdapter.getRemoteDevice(this.deviceAddress);
	}

	/**
	 * ��ȡ�豸����
	 * 
	 * @return String
	 */
	public String getDeviceName() {
		return this.device.getName();
	}

	/**
	 * ���������豸
	 */
	public boolean connect() {
		if (!this.isConnection) {
			try {
				bluetoothSocket = this.device
						.createRfcommSocketToServiceRecord(uuid);
				bluetoothSocket.connect();
				outputStream = bluetoothSocket.getOutputStream();
				this.isConnection = true;
				if (this.bluetoothAdapter.isDiscovering()) {
					System.out.println("Close Adapter!");
					this.bluetoothAdapter.isDiscovering();
				}
			} catch (Exception e) {
				Toast.makeText(this.context, "Connect failed!", 1).show();
				return false;
			}
			Toast.makeText(this.context, this.device.getName() + "Connect successed!",
					Toast.LENGTH_SHORT).show();
			return true;
		} else {
			return true;
		}
	}

	/**
	 * �Ͽ������豸����
	 */
	public static void disconnect() {
		System.out.println("Disconect bluetooth device");
		try {
			bluetoothSocket.close();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ѡ��ָ��
	 * selectCommand()
	 */
	public void selectCommand() {
		new AlertDialog.Builder(context).setTitle("Please Choose Command")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (isConnection) {
							try {
								outputStream.write(byteCommands[which]);

							} catch (IOException e) {
								Toast.makeText(context, "Failed to set Command!",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(context, "Connect failed, please reconnect.",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).create().show();
	}

	/**
	 * ��������
	 * send data
	 */
//	public void send(String sendData) {
//		if (this.isConnection) {
//			System.out.println(sendData);
//			try {
//				byte[] data = sendData.getBytes("gbk");
//				byte[] commmand = {0x55, 0x55, 0x32, 0x35, 0x38, 0x0D, 0x64,0x02,0x01,0x02,0x05,0x61,0x00,0x64,0x01};
//				Log.d("senddata", sendData);
//				outputStream.write(commmand, 0, commmand.length);
//				outputStream.flush();
//			} catch (IOException e) {
//				Toast.makeText(this.context, "Failed to send out!", Toast.LENGTH_SHORT)
//						.show();
//			}
//		} else {
//			Toast.makeText(this.context, "Connect failed, please reconnect.", Toast.LENGTH_SHORT)
//					.show();
//
//		}
//	}
	
	public void sendCommandWithDelay(byte[] command) throws InterruptedException {
		if (this.isConnection) {
//			System.out.println(sendData);
			try {
//				byte[] command1 = {0x55,0x55,0x00,0x01,0x02,0x0b,0x02,0x05,0x00,0x00,0x01,0x00,'K',0x01,'F',0x01,'C',0x01,'C',0x01,'C',0x01};
				for(byte bt:command){
				Log.d("BTest", bt+"");
				outputStream.write(bt);
				outputStream.flush();
				Thread.sleep(0, 500000);
				}
			} catch (IOException e) {
				Toast.makeText(this.context, "Failed to send out!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this.context, "Connect failed, please reconnect.", Toast.LENGTH_SHORT)
					.show();

		}
	}
	
	public void sendDataWithDelay(String sendData) throws InterruptedException {
		if (this.isConnection) {
//			System.out.println(sendData);
			try {
				byte[] command = {0x55,0x55,0x00,0x01,0x02,0x0d,0x02,0x05,0x00,0x00,0x01,0x00};
				command[7] = (byte) sendData.length();
				byte[] data = sendData.getBytes("gbk");
				byte[] newdata = new byte[data.length*2];
				int index = 0;
				for(int i = 0, len = data.length;i < len;i++){
					newdata[index] = data[i];
					index++;
					newdata[index] = (byte) (index % 3);
					index++;
				}
				data = addByteArrays(command,newdata);
				for(byte bt:data){
				outputStream.write(bt);
				outputStream.flush();
				Thread.sleep(0, 500000);
				}
			} catch (IOException e) {
				Toast.makeText(this.context, "Failed to send out!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this.context, "Connect failed, please reconnect.", Toast.LENGTH_SHORT)
					.show();

		}
	}
	/**
	 * ���ʹ�ָ������
	 * send data with command index
	 */
	public void sendwithCommand(String sendData, int which) {
		if (this.isConnection) {
			System.out.println("Printing!");
			try {
				outputStream.write(byteCommands[which]);
				byte[] data = sendData.getBytes("gbk");
				outputStream.write(data, 0, data.length);
				outputStream.flush();
			} catch (IOException e) {
				Toast.makeText(this.context, "Failed to send out!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this.context, "Connect failed, please reconnect.", Toast.LENGTH_SHORT)
					.show();

		}
	}
	
	/**
	 * ������������BitMap��ӡ
	 * send parameter of Barcode and Print it out
	 * @throws WriterException 
	 */
	public void sendBitMapParam(String content, int imgWidth, int imgHeight) throws WriterException {
		if (this.isConnection) {
			System.out.println("Printing!");
			try {
				Bitmap bm = BarcodeUtil.writeCode128(content, imgWidth, imgHeight);
				for (int i = 0; i < 2; i++)
					outputStream.write(PrinterCommands.FEED_LINE); // Feed Lines for Barcode Printing
				outputStream.write(PrinterCommands.START, 0, PrinterCommands.START.length);				
                byte[] buffer = getReadBitMapBytes(bm);
//                fin.close();
                outputStream.write(buffer, 0, buffer.length);// �����ļ�buffer[]
				outputStream.write(PrinterCommands.END, 0, PrinterCommands.END.length);
//                byte[] data = new byte[1];
//                data[0] = 13;// ��һ�����з� 13
//				outputStream.write(data, 0, data.length);
				outputStream.flush();
			} catch (IOException e) {
				Toast.makeText(this.context, "Failed to send out!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this.context, "Connect failed, please reconnect.", Toast.LENGTH_SHORT)
					.show();

		}
	}
	
	/**����ͼƬ ��ȡ��ӡ����**/
	private byte[] getReadBitMapBytes(Bitmap bitmap) {
		/***ͼƬ���ˮӡ**/
		//bitmap = createBitmap(bitmap);
		byte[] bytes = null;  //��ӡ����
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		System.out.println("width=" + width + ", height=" + height);
		int heightbyte = (height - 1) / 8 + 1;
		int bufsize = width * heightbyte;
		int m1, n1;
        byte[] maparray = new byte[bufsize];
        
        byte[] rgb = new byte[3];
        
        int []pixels = new int[width * height]; //ͨ��λͼ�Ĵ�С�������ص�����
        
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        /**����ͼƬ ��ȡλͼ����**/
		for (int j = 0;j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = pixels[width * j + i]; /**��ȡ�ңǣ�ֵ**/
				int r = Color.red(pixel);
				int g = Color.green(pixel);
				int b = Color.blue(pixel);
				//System.out.println("i=" + i + ",j=" + j + ":(" + r + ","+ g+ "," + b + ")");
				rgb[0] = (byte)r;
				rgb[1] = (byte)g;
				rgb[2] = (byte)b;
				 if (r != 255 || g != 255 || b != 255){//������ǿհ׵Ļ��ú�ɫ���    �������ͯЬҪ������ɫ�����ﴦ�� 
                     m1 = (j / 8) * width + i;
                     n1 = j - (j / 8) * 8;
                     maparray[m1] |= (byte)(1 << 7 - ((byte)n1));
                 }
			}
		}
		byte[] b = new byte[width];
		int line = 0;
		int j = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		/**��λͼ���ݽ��д���**/
		for (int i = 0; i < maparray.length; i++) {
			b[j] = maparray[i];
			j++;
			if (j == width) {  /**  322ͼƬ�Ŀ� **/
				if (line < ((width - 1) / 8)) {
					byte[] lineByte = new byte[width+7];
					byte nL = (byte) width;
					byte nH = (byte) (width >> 8);
					int index = 5;
					/**��Ӵ�ӡͼƬǰ���ַ�  ÿ�е� ������8λ**/
					lineByte[0] = 0x1B;
					lineByte[1] = 0x2A;
					lineByte[2] = 1;
					lineByte[3] = nL;
					lineByte[4] = nH;
					/**copy ��������**/
					System.arraycopy(b, 0, lineByte, index, b.length);

					lineByte[lineByte.length - 2] = 0x0D;
					lineByte[lineByte.length - 1] = 0x0A;
					baos.write(lineByte, 0, lineByte.length);
					try {
						baos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					line++;
				}
				j = 0;
			}
		}
		bytes = baos.toByteArray();
		return bytes;
	}

	/**
	 * send Byte Array
	 * it can be Byte command or String
	 * @param data
	 */
	public void sendByteArray(byte[] data) {
		// TODO Auto-generated method stub
		if (this.isConnection) {
			try {
				outputStream.write(data);
				outputStream.flush();
			} catch (IOException e) {
				Toast.makeText(this.context, "Failed to send out!", Toast.LENGTH_SHORT)
				.show();
			}
		} else {
			Toast.makeText(this.context, "Connect failed, please reconnect.", Toast.LENGTH_SHORT)
					.show();
		}
	}
	/**
	 * ���ʹ�ָ������
	 * feed and cut
	 */
	public void sendFeedCutCommand() {
		if (this.isConnection) {
			try {
				outputStream.write(PrinterCommands.FEED_PAPER_AND_CUT);
				outputStream.flush();
			} catch (IOException e) {
				Toast.makeText(this.context, "Failed to send out!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this.context, "Connect failed, please reconnect.", Toast.LENGTH_SHORT)
					.show();

		}
	}
	
	private byte[] addByteArrays(byte[] a, byte[] b){
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}