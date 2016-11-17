package com.challenge1.service.watcher;

import com.challenge1.service.FileHandlerImpl;
import com.challenge1.service.api.FileModificationListener;
import com.challenge1.service.api.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Service
public class DirectoryRegistration {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Map<WatchKey, Path> keys;
    private WatchService watcher;
    private final FileModificationListener listener;
    boolean initialRegister = true;

    public DirectoryRegistration(FileModificationListener listener) {
        this.listener = listener;
        this.keys = new HashMap<>();
        try {
            this.watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    public void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            listener.onAction(new FileHandlerImpl(dir, Type.ENTRY_CREATE));

        if (true) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);

            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    public void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        initialRegister = false;
    }

    public Map<WatchKey, Path> getKeys() {
        return keys;
    }

    public WatchKey getKey() {
        try {
            return watcher.take();
        } catch (InterruptedException e) {
            LOG.error("There was an error while trying take event from watch key. ORIGINAL ERROR: ", e);
        }
        return null;
    }
}
