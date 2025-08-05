package com.qrvault.database;

import android.content.Context;

import com.qrvault.models.QRCode;

import java.util.List;

public class QRRepository {
	private QRDao dao;

	public QRRepository(Context context) {
		dao = new QRDao(context);
	}

	public long saveQRCode(QRCode code) {
		return dao.insertQRCode(code);
	}

	public List<QRCode> loadAllQRCodes() {
		return dao.getAllQRCodes();
	}

	public void update(QRCode code) {
		dao.updateQRCode(code);
	}

	public void delete(int id) {
		dao.deleteQRCode(id);
	}
}
