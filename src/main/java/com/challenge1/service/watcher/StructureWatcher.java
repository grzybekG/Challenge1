package com.challenge1.service.watcher;

import com.challenge1.service.FileHandlerImpl;
import com.challenge1.service.NodeLogic;
import com.challenge1.service.api.FileModificationListener;
import com.challenge1.service.api.Node;
import com.challenge1.service.api.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class StructureWatcher implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StructureWatcher.class);
    private Path path;
    private Map<WatchKey, Path> watchMap = new HashMap<>();
    private WatchService watchService;
    private FileModificationListener listener;


    public StructureWatcher(Path path, FileModificationListener listener) {
        this.path = path;
        this.listener = listener;
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            registerSingle(path);
            registerAll(path);

        } catch (IOException e) {
            LOG.error("Error in initialization of structure watcher", e);
        }
    }

    private void registerAll(Path path) throws IOException {
        LOG.info("Incming path {}", path);
        for (Node<Path> singlePath : NodeLogic.getNodeIterator(new FileHandlerImpl(path))) {
            if (singlePath.getData().toFile().isDirectory()) {
                LOG.info("Registering path {}", singlePath.getData());
                registerSingle(singlePath.getData());
            }
        }
    }

    public void registerSingle(Path singlePath) throws IOException {
        WatchKey register = singlePath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        watchMap.put(register, singlePath);
    }

    private void registerWatcher() throws IOException {
        WatchKey watchKey;
        for (; ; ) {
            try {
                watchKey = watchService.take();
            } catch (InterruptedException e) {
                LOG.error("Cannot take from watch service", e);
                return;
            }
            Path directory = watchMap.get(watchKey);


            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                Path eventPath = (Path) event.context();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    registerAll(eventPath.toAbsolutePath());

                    listener.onAction(new FileHandlerImpl(eventPath, Type.ENTRY_CREATE));

                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    registerAll(eventPath.toAbsolutePath());
                    listener.onAction(new FileHandlerImpl(eventPath, Type.ENTRY_MODIFY));
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    listener.onAction(new FileHandlerImpl(eventPath, Type.ENTRY_DELETE));
                }
            }
            //
            if (!watchKey.reset()) {
                watchMap.remove(watchKey);
            }

        }

    }


    @Override
    public void run() {
        try {
            registerWatcher();
        } catch (IOException e) {
            LOG.error("There was an error during file structure watcher initialization.");
        }
    }
}
