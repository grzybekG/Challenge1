package com.challenge1.service.watcher;

import com.challenge1.service.FileHandlerImpl;
import com.challenge1.service.NodeLogic;
import com.challenge1.service.api.FileModificationListener;
import com.challenge1.service.api.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryRegistration {
    private static final Logger LOG = LoggerFactory.getLogger(DirectoryRegistration.class);
    private Map<WatchKey, Path> keys = new HashMap<>();
    private WatchService watchService;
    private FileModificationListener listener;
    boolean initialRegister = true;
    private NodeLogic nodeLogic;

    /**
     *
     * @param watchService
     * @param nodeLogic
     * @param listener
     */
    public DirectoryRegistration(WatchService watchService, NodeLogic nodeLogic, FileModificationListener listener) {
        this.watchService = watchService;
        this.nodeLogic = nodeLogic;
        this.listener = listener;
    }

    /**
     * Initialization of registration - should be called after constructing object with listener
     * @param path
     */
    public  void init(Path path){
        registerAll(path);
        initialRegister = false;
    }

    public void registerSingle(Path dir) {
        WatchKey key = null;
        try {
            if (dir.toFile().isDirectory()) {
                LOG.info("Registering [{}]", dir);
                key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            }else {
                return;
            }

            if (!initialRegister) {
                Path prev = keys.get(key);

                if (prev == null) {
                    listener.onAction(new FileHandlerImpl(dir, Type.ENTRY_CREATE));
                    LOG.info("registering new path: {}\n", dir);

                } else {
                    if (!dir.equals(prev)) {
                        listener.onAction(new FileHandlerImpl(dir, Type.ENTRY_MODIFY));
                        LOG.info("update: {} to  {}\n", prev, dir);
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
    public void registerAll(final Path root) {
        registerSingle(root);

        nodeLogic.getNodeIterator(new FileHandlerImpl(root)).forEach(node -> registerSingle(node.getData()));

    }

    public Map<WatchKey, Path> getKeys() {
        return keys;
    }

    public WatchKey getKey() {
        try {
            return watchService.take();
        } catch (InterruptedException e) {
            LOG.error("There was an error while trying take event from watch key. ORIGINAL ERROR: ", e);
            return null;
        }
    }


    public void removeKey(WatchKey key) {
        listener.onAction(new FileHandlerImpl(keys.get(key), Type.ENTRY_DELETE));
        keys.remove(key);

    }
}
