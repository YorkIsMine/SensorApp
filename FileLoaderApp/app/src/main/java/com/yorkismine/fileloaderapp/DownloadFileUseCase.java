package com.yorkismine.fileloaderapp;

import com.yorkismine.fileloaderapp.model.DownloadedItem;

import java.util.List;

public interface DownloadFileUseCase {
    List<DownloadedItem> downloadFile(String url, String dir);
}
