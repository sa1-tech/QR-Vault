package com.qrvault.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.qrvault.models.QRCode;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportUtils {

	private static final String EXPORT_DIR = "QRVault/Exports";

	private static String getTimeStampedFileName(String base, String extension) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		return base + "_" + timestamp + "." + extension;
	}

	public static void exportToCSV(@NonNull Context context, @NonNull List<QRCode> qrList) {
		try {
			String fileName = getTimeStampedFileName("qr_export", "csv");
			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
			values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
			values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/" + EXPORT_DIR);

			Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
			if (uri == null) throw new Exception("Cannot create file Uri");

			try (OutputStream os = context.getContentResolver().openOutputStream(uri)) {
				if (os == null) throw new Exception("Failed to open output stream");

				StringBuilder sb = new StringBuilder();
				sb.append("Title,Content,Date\n");
				for (QRCode qr : qrList) {
					sb.append(escapeCSV(qr.getTitle())).append(",")
							.append(escapeCSV(qr.getContent())).append(",")
							.append(escapeCSV(formatTimestamp(qr.getTimestamp()))).append("\n");
				}

				os.write(sb.toString().getBytes());
				os.flush();
			}

			Toast.makeText(context, "CSV exported to Documents/" + EXPORT_DIR, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(context, "CSV Export Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public static void exportToPDF(@NonNull Context context, @NonNull List<QRCode> qrList) {
		try {
			String fileName = getTimeStampedFileName("qr_export", "pdf");
			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
			values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
			values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/" + EXPORT_DIR);

			Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
			if (uri == null) throw new Exception("Cannot create file Uri");

			try (OutputStream os = context.getContentResolver().openOutputStream(uri)) {
				if (os == null) throw new Exception("Failed to open output stream");

				Document document = new Document();
				PdfWriter.getInstance(document, os);
				document.open();

				for (QRCode qr : qrList) {
					document.add(new Paragraph("Title: " + qr.getTitle()));
					document.add(new Paragraph("Content: " + qr.getContent()));
					document.add(new Paragraph("Date: " + formatTimestamp(qr.getTimestamp())));
					document.add(new Paragraph("----------------------------------------"));
				}

				document.close();
			}

			Toast.makeText(context, "PDF exported to Documents/" + EXPORT_DIR, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(context, "PDF Export Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private static String escapeCSV(String input) {
		if (input == null) return "";
		boolean needsQuotes = input.contains(",") || input.contains("\"") || input.contains("\n");
		String escaped = input.replace("\"", "\"\"");
		return needsQuotes ? "\"" + escaped + "\"" : escaped;
	}

	private static String formatTimestamp(long timestamp) {
		if (timestamp <= 0) return "";
		return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(timestamp));
	}
}
