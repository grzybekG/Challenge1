package com.challenge1.service.watcher;

import com.challenge1.service.FileHandlerImpl;
import com.challenge1.service.NodeLogic;
import com.challenge1.service.api.FileModificationListener;
import com.challenge1.service.api.Node;
import com.challenge1.service.api.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class DirectoryRegistration {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Map<WatchKey, Path> keys;
    private WatchService watcher;
    boolean initialRegister = true;
    private FileModificationListener listener;

    public DirectoryRegistration(Path path, FileModificationListener listener) {
        this.listener = listener;
        this.keys = new HashMap<>();
        try {
            this.watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        registerAll(path);
    }

    public void register(Path dir) {
        WatchKey key = null;
        try {
            if (dir.toFile().isDirectory()) {
                key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            }

            if (!initialRegister) {
                Path prev = keys.get(key);
                ; //FIXME
                if (prev == null) {
                    listener.onAction(new FileHandlerImpl(dir, Type.ENTRY_CREATE));
                    System.out.format("register: %s\n", dir);

                } else {
                    if (!dir.equals(prev)) {
                        listener.onAction(new FileHandlerImpl(dir, Type.ENTRY_MODIFY));
                        System.out.format("update: %s -> %s\n", prev, dir);
                    }
                }
            }
            keys.put(key, dir);
        } catch (IOException e) {
            LOG.error("There was exception during path registration -[{}]. ", dir, e);
        }
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    public void registerAll(final Path start) {
        try {
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    register(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            throw new NullPointerException("WatchKey null...");
        }
    }
}
