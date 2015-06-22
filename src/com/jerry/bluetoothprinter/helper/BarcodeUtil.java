package com.jerry.bluetoothprinter.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * @author LDM
 * @fileName BarcodeUtil.java
 * @data 2012-9-5 下午3:36:25
 * 条码生成工具
 */
public class BarcodeUtil {

	/**
	 * 生成二维码
	 * @param content 数据内容
	 * @param coding  编码如utf-8
	 * @param imgWidth 生成图片宽度
	 * @param imgHeight 生成图片高度
	 * @return Bitmap对象
	 * @throws WriterException
	 */
	public static Bitmap writeQR(String content, String coding, int imgWidth, int imgHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, coding);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, imgWidth, imgHeight, hints);
		return BitMatrixToBitmap(bitMatrix);
	}

	/**
	 * 生成条形码
	 * @param content 数据内容
	 * @param imgWidth 生成图片宽度
	 * @param imgHeight 生成图片高度
	 * @return Bitmap对象
	 * @throws WriterException
	 */
	public static Bitmap writeCode128(String content, int imgWidth, int imgHeight) throws WriterException {
		int codeWidth = 3 + (7 * 6) + 5 + (7 * 6) + 3;
		codeWidth = Math.max(codeWidth, imgWidth);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, codeWidth, imgHeight,null);
		return BitMatrixToBitmap(bitMatrix);
	}

	/**
	 * BitMatrix转换成Bitmap
	 * @param matrix
	 * @return
	 */
	private static Bitmap BitMatrixToBitmap(BitMatrix matrix) {
		final int WHITE = 0xFFFFFFFF;
		final int BLACK = 0xFF000000;

		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
			}
		}
		return createBitmap(width, height, pixels);
	}

	/**
	 * 生成Bitmap
	 * @param width
	 * @param height
	 * @param pixels
	 * @return
	 */
	private static Bitmap createBitmap(int width, int height, int[] pixels) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	
}
