package com.qrvault.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qrvault.models.QRCode;

import java.util.ArrayList;
import java.util.List;

public class QRDatabaseHelper extends SQLiteOpenHelper {

	// Table + Columns
	public static final String TABLE_QR = "qr_codes";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_IS_FAVORITE = "is_favorite";
	public static final String COLUMN_TAGS = "tags";
	public static final String COLUMN_IMAGE_PATH = "image_path"; // ✅ NEW COLUMN

	// DB Configuration
	private static final String DATABASE_NAME = "qrvault.db";
	private static final int DATABASE_VERSION = 4; // ⬆️ Bumped version for schema change

	public QRDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Table Creation
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_QR + " (" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_TITLE + " TEXT," +
				COLUMN_CONTENT + " TEXT," +
				COLUMN_TYPE + " TEXT," +
				COLUMN_TIMESTAMP + " INTEGER," +
				COLUMN_IS_FAVORITE + " INTEGER," +
				COLUMN_TAGS + " TEXT," +
				COLUMN_IMAGE_PATH + " TEXT" + // ✅ Added to schema
				")";
		db.execSQL(CREATE_TABLE);
	}

	// Upgrade logic
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QR);
		onCreate(db); // In production, you'd use ALTER TABLE instead of drop+create
	}

	// Insert QRCode
	public boolean insertQRCode(QRCode qr) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = mapQRCodeToValues(qr);
		long result = db.insert(TABLE_QR, null, values);
		db.close();
		return result != -1;
	}

	// Bulk Insert
	public void insertBulkQR(List<QRCode> qrList) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			for (QRCode qr : qrList) {
				db.insert(TABLE_QR, null, mapQRCodeToValues(qr));
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	// Update QRCode
	public boolean updateQRCode(QRCode qr) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = mapQRCodeToValues(qr);
		int rows = db.update(TABLE_QR, values, COLUMN_ID + " = ?", new String[]{String.valueOf(qr.getId())});
		db.close();
		return rows > 0;
	}

	// Get All QR Codes
	public List<QRCode> getAllQRCodes() {
		List<QRCode> qrCodes = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_QR, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

		if (cursor != null && cursor.moveToFirst()) {
			do {
				qrCodes.add(cursorToQR(cursor));
			} while (cursor.moveToNext());
			cursor.close();
		}

		db.close();
		return qrCodes;
	}

	// Get Favorites
	public List<QRCode> getFavoriteQRCodes() {
		List<QRCode> favs = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_QR, null, COLUMN_IS_FAVORITE + "=?", new String[]{"1"}, null, null, COLUMN_TIMESTAMP + " DESC");

		if (cursor != null && cursor.moveToFirst()) {
			do {
				favs.add(cursorToQR(cursor));
			} while (cursor.moveToNext());
			cursor.close();
		}

		db.close();
		return favs;
	}

	// Delete by ID
	public boolean deleteQRCode(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		int rows = db.delete(TABLE_QR, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
		db.close();
		return rows > 0;
	}

	// Clear All
	public void clearAllQR() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QR, null, null);
		db.close();
	}

	// Toggle Favorite
	public boolean toggleFavorite(int id, boolean isFav) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_IS_FAVORITE, isFav ? 1 : 0);
		int rows = db.update(TABLE_QR, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
		db.close();
		return rows > 0;
	}

	// Utility: Convert Cursor to QRCode
	private QRCode cursorToQR(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
		String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
		String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
		String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
		long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
		boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1;
		String tags = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS));
		String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)); // ✅ Added

		return new QRCode(id, title, content, imagePath, timestamp, isFavorite, type, tags);
	}

	// Utility: Convert QRCode to ContentValues
	private ContentValues mapQRCodeToValues(QRCode qr) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_TITLE, qr.getTitle());
		values.put(COLUMN_CONTENT, qr.getContent());
		values.put(COLUMN_TYPE, qr.getType());
		values.put(COLUMN_TIMESTAMP, qr.getTimestamp());
		values.put(COLUMN_IS_FAVORITE, qr.isFavorite() ? 1 : 0);
		values.put(COLUMN_TAGS, qr.getTags());
		values.put(COLUMN_IMAGE_PATH, qr.getImagePath()); // ✅ Added
		return values;
	}
}
