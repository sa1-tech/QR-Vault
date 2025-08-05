package com.qrvault.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRUtils {

	public static Bitmap generateQRBitmap(String data, int size) throws WriterException {
		BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
		Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				bitmap.setPixel(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}

		return bitmap;
	}
}
