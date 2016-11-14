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

public class StructureWatcher {
    private static final Logger LOG = LoggerFactory.getLogger(StructureWatcher.class);
    private Path path;
    private IMyListener listener;
    Map<WatchKey, Path> watchMap = new HashMap<>();


    public StructureWatcher(Path path, IMyListener listener) {
        this.path = path;
        this.listener = listener;
    }

    public void startWatching() {
        try {
            registerWatcher(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerWatcher(Path path) throws IOException {
        List<Path> pathList = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                pathList.add(file);
                return FileVisitResult.CONTINUE;
            }
        });
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

            for (Path singlePath : pathList) {
                WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                listener.onAction(singlePath);
                watchMap.put(watchKey, singlePath);
                //todo read this  http://queue.acm.org/detail.cfm?id=2169076
                //https://egghead.io/courses/introduction-to-reactive-programming
                //https://docs.oracle.com/javase/tutorial/essential/io/examples/WatchDir.java (WatchDir Example)

            }
            WatchKey watchKey;
            do {
                watchKey = watchService.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path eventPath = (Path) event.context();

                }
            }
            while (watchKey.reset());

        } catch (Exception ex) {
            LOG.error("Unexpected happend in ", ex);
        }

    }


}
