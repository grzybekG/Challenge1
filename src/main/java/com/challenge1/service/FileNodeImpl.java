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
    private Iterator<Node<Path>> statefulIterator;

    public FileNodeImpl(Path path) {
        this.path = path;
    }

    public Iterator<Node<Path>> iterator() {

        List<Node<Path>> result = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            Iterator<Path> iterator = directoryStream.iterator();
            while (iterator.hasNext()) {
                result.add(new FileNodeImpl(iterator.next()));
            }
        } catch (IOException ex) {
            //proper exc handling
        }
        return result.iterator();
    }

    @Override
    public Iterator<Node<Path>> getChildren() {
        if (statefulIterator == null) {
            statefulIterator = iterator();
        }
        return statefulIterator;
    }

    @Override
    public Path getData() {
        return path;
    }
}
