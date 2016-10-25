package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


class FileNodeImpl implements Node<Path> {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Path path;

    private Iterator<Node<Path>> iterator;

    public FileNodeImpl(Path path) {
        this.path = path;
    }

    public Iterator<Node<Path>> iterator() {
        List<Node<Path>> nodes = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            directoryStream.forEach(p -> nodes.add(new FileNodeImpl(p)));
        } catch (IOException ex) {
            //FIXME 2x logging?!?!
            LOG.info("There was an exception when getting directoryStream - original message {}", ex.getMessage());
        }

        iterator = nodes.iterator();

        return iterator;
    }


    @Override
    public Iterable<Node<Path>> getChildren() {
        if (iterator == null) {
            iterator();
        }
        if (iterator.hasNext()) {
            return new FileNodeImpl(iterator.next().getData());
        }
        return new ArrayList<>();
    }

    @Override
    public Path getData() {
        return path;
    }
}