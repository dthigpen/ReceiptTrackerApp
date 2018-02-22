package com.davidthigpen.receiptreader.data.util;

import java.io.File;

abstract public class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
