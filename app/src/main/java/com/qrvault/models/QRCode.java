package com.qrvault.models;

import android.database.Cursor;

import com.qrvault.database.QRDatabaseHelper;

import java.io.Serializable;

public class QRCode implements Serializable {
	private int id;
	private String title;
	private String content;
	private String imagePath;
	private long timestamp;
	private boolean isFavorite;
	private String type;
	private String tags;

	public QRCode(int id, String title, String content, String imagePath, long timestamp, boolean isFavorite, String type, String tags) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.imagePath = imagePath;
		this.timestamp = timestamp;
		this.isFavorite = isFavorite;
		this.type = type;
		this.tags = tags;
	}

	public static QRCode fromCursor(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_ID));
		String title = cursor.getString(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_TITLE));
		String content = cursor.getString(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_CONTENT));
		String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_IMAGE_PATH));
		long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_TIMESTAMP));
		boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_IS_FAVORITE)) == 1;
		String type = cursor.getString(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_TYPE));
		String tags = cursor.getString(cursor.getColumnIndexOrThrow(QRDatabaseHelper.COLUMN_TAGS));
		return new QRCode(id, title, content, imagePath, timestamp, isFavorite, type, tags);
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean favorite) {
		isFavorite = favorite;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
