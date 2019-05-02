package com.museumsystem.museumserver.utlis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeGenerator {

	public static byte[] generateQr(String text, int width, int height) throws WriterException, IOException {
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", arrayOutputStream);
		
		return arrayOutputStream.toByteArray();
	}
}
