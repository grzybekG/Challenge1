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

/**
 * Created by mlgy on 18/10/2016.
 */
public class FileNodeImpl implements Node<Path> {
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Path path;
    private Iterator<Node> iterator;

    public FileNodeImpl(Path path) {
        this.path = path;
    }

    public Iterator<Node> iterator() {
        List<Node> nodes = new ArrayList<>();
        if (iterator == null) {
       //TODO     FileUtils.iterateFiles(new File(path.toString()), FalseFileFilter.INSTANCE, DirectoryFileFilter.INSTANCE);
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                directoryStream.forEach(p -> nodes.add(new FileNodeImpl(p)));
            } catch (IOException ex) {
                LOG.info("There was an exception when getting directoryStream - original message {}",ex.getMessage());
            }
            iterator = new NodeIterator(nodes);

        }
        return iterator;
    }

    @Override
    public Iterable<Node> getChildren() {

        return new FileNodeImpl(path);


    }

    @Override
    public Path getData() {
        return path;
    }
}