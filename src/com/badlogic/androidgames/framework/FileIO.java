package com.badlogic.androidgames.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

public class FileIO {
	AssetManager assets;
	String externalStoragePath;

	public FileIO(AssetManager assets) {
		this.assets = assets;
		this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public InputStream readAsset(String fileName) throws IOException {
		return assets.open(fileName);
	}

	public InputStream readFile(String fileName) throws IOException {
		return new FileInputStream(externalStoragePath + File.separator + fileName);
	}

	public OutputStream writeFile(String fileName) throws IOException {
		// fileName может содержать папку
		// ищу сепаратор
		int k = fileName.lastIndexOf(File.separator);
		if (k > -1) {
			String filePath = externalStoragePath + File.separator + fileName.substring(0, k);
			// проверяю доступ к папке
			File path = new File(filePath);
			if (!path.exists()) {
				Boolean res = path.mkdir();
				if(res) {
					path.exists();
				}
			}
		}
		// создаю стрим файла
		String filePath = externalStoragePath + File.separator + fileName;
		return new FileOutputStream(filePath);
	}
	
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
}