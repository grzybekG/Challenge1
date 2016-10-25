package com.challenge1.service.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mlgy on 20/10/2016.
 */
public class StructureWatcher<T> implements Runnable{
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    Path path;
    WatchService watcher;



    public static void  registerWatcher(Path path) throws IOException {
        List<Path> pathList = new ArrayList<>();
        Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
        @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException{
            //LOG.info("FVR: " +file);
            pathList.add(file);
            return FileVisitResult.CONTINUE;
        }
        });
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> someMap = new HashMap<>();
         //   for (Path path : pathList) {
           //     someMap.put(path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE), path);
            //}
            WatchKey watchKey;
            do {
                watchKey = watchService.take();
                Path eventDirectory = someMap.get(watchKey);
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path eventPath = (Path) event.context();
                    System.out.println("Sth Happend " + event.kind() + "In path [" + eventPath + "]");
                }
            }
            while (watchKey.reset());

        } catch (Exception ex) {
        }

    }

    @Override
    public void run() {

    }
}
