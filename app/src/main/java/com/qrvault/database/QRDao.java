package com.qrvault.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qrvault.models.QRCode;

import java.util.ArrayList;
import java.util.List;

public class QRDao {
	private QRDatabaseHelper dbHelper;

	public QRDao(Context context) {
		dbHelper = new QRDatabaseHelper(context);
	}

	public long insertQRCode(QRCode code) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(QRDatabaseHelper.COLUMN_TITLE, code.getTitle());
		values.put(QRDatabaseHelper.COLUMN_CONTENT, code.getContent());
		values.put(QRDatabaseHelper.COLUMN_TYPE, code.getType());
		values.put(QRDatabaseHelper.COLUMN_TIMESTAMP, code.getTimestamp());
		values.put(QRDatabaseHelper.COLUMN_IS_FAVORITE, code.isFavorite() ? 1 : 0);
		values.put(QRDatabaseHelper.COLUMN_TAGS, code.getTags());
		values.put(QRDatabaseHelper.COLUMN_IMAGE_PATH, code.getImagePath()); // ✅ Added

		return db.insert(QRDatabaseHelper.TABLE_QR, null, values);
	}

	public List<QRCode> getAllQRCodes() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<QRCode> codes = new ArrayList<>();

		Cursor cursor = db.query(
				QRDatabaseHelper.TABLE_QR,
				null,
				null,
				null,
				null,
				null,
				QRDatabaseHelper.COLUMN_TIMESTAMP + " DESC"
		);

		if (cursor.moveToFirst()) {
			do {
				codes.add(QRCode.fromCursor(cursor));
			} while (cursor.moveToNext());
		}

		cursor.close();
		return codes;
	}

	public void updateQRCode(QRCode code) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(QRDatabaseHelper.COLUMN_TITLE, code.getTitle());
		values.put(QRDatabaseHelper.COLUMN_CONTENT, code.getContent());
		values.put(QRDatabaseHelper.COLUMN_TYPE, code.getType());
		values.put(QRDatabaseHelper.COLUMN_TIMESTAMP, code.getTimestamp());
		values.put(QRDatabaseHelper.COLUMN_IS_FAVORITE, code.isFavorite() ? 1 : 0);
		values.put(QRDatabaseHelper.COLUMN_TAGS, code.getTags());
		values.put(QRDatabaseHelper.COLUMN_IMAGE_PATH, code.getImagePath()); // ✅ Added

		db.update(
				QRDatabaseHelper.TABLE_QR,
				values,
				QRDatabaseHelper.COLUMN_ID + " = ?",
				new String[]{String.valueOf(code.getId())}
		);
	}

	public void deleteQRCode(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(
				QRDatabaseHelper.TABLE_QR,
				QRDatabaseHelper.COLUMN_ID + " = ?",
				new String[]{String.valueOf(id)}
		);
	}
}
