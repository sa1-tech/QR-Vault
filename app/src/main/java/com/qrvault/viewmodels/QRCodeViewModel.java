package com.qrvault.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qrvault.database.QRRepository;
import com.qrvault.models.QRCode;

import java.util.List;

public class QRCodeViewModel extends AndroidViewModel {
	private QRRepository repository;
	private MutableLiveData<List<QRCode>> qrCodesLiveData;

	public QRCodeViewModel(@NonNull Application application) {
		super(application);
		repository = new QRRepository(application);
		qrCodesLiveData = new MutableLiveData<>();
		loadQRCodes();
	}

	public LiveData<List<QRCode>> getQrCodes() {
		return qrCodesLiveData;
	}

	public void loadQRCodes() {
		qrCodesLiveData.setValue(repository.loadAllQRCodes());
	}

	public void addQRCode(QRCode code) {
		repository.saveQRCode(code);
		loadQRCodes();
	}

	public void updateQRCode(QRCode code) {
		repository.update(code);
		loadQRCodes();
	}

	public void deleteQRCode(int id) {
		repository.delete(id);
		loadQRCodes();
	}
}
