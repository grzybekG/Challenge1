package com.challenge1.service.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;


public class StructureWatcher {
    private static final Logger LOG = LoggerFactory.getLogger(StructureWatcher.class);
    private final DirectoryRegistration directoryRegistration;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    public StructureWatcher(DirectoryRegistration directoryRegistration) {
        this.directoryRegistration = directoryRegistration;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    public void processEvents() {
        for (; ; ) {
            // wait for key to be signalled
            WatchKey key = directoryRegistration.getKey();


            Path dir = directoryRegistration.getKeys().get(key);
            if (dir == null) {
                LOG.warn("WatchKey not recognized while processing events!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);


                // if directory is created, and watching recursively, then
                // registerSingle it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                        directoryRegistration.registerAll(child);
                    }
                }
                if (kind == ENTRY_DELETE) {
                    directoryRegistration.removeKey(key);
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                directoryRegistration.getKeys().remove(key);

                // all directories are inaccessible
                if (directoryRegistration.getKeys().isEmpty()) {
                    break;
                }
            }
        }
    }

}

