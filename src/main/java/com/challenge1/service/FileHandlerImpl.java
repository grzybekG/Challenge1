package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


class FileHandlerImpl implements Node<Path> {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Path path;

    private Iterator<Node<Path>> iterator = Collections.emptyIterator();

    FileHandlerImpl(Path path) {
        this.path = path;
    }

    public Iterator<Node<Path>> iterator() {
        List<Node<Path>> nodes = new ArrayList<>();

        if (path != null && path.toFile().isDirectory()) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                directoryStream.forEach(p -> nodes.add(new FileHandlerImpl(p)));
                iterator = nodes.iterator();
            } catch (IOException ex) {
                LOG.error("There was an exception when getting directoryStream - original message {}, FULL STACK : {}", ex.getMessage(), ex);
            }
        }
        return iterator;
    }

    @Override
    public Iterable<Node<Path>> getChildren() {

        if (iterator.hasNext()) {
            return new FileHandlerImpl(iterator.next().getData());
        }
        return new ArrayList<>();
    }

    @Override
    public Path getData() {
        return path;
    }
}