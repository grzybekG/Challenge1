package com.challenge1.service.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

public class WatchServiceFactory {

    public static WatchService getWatchService() {
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return watchService;
    }
}
