package com.challenge1.service;

import com.challenge1.service.api.Node;

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

    private Path path;
    Iterator<Node> iterator;

    public FileNodeImpl(Path path) {
        this.path = path;
    }

    public Iterator<Node> iterator() {
        List<Node> list = new ArrayList<>();
        if (iterator == null) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                Iterator<Path> iterator = directoryStream.iterator();
                while (iterator.hasNext()) {
                    list.add(new FileNodeImpl(iterator.next()));
                }
            } catch (IOException ex) {
                //proper exc handling
            }
            iterator = new NodeIterator(list.toArray(new Node[list.size()]));

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