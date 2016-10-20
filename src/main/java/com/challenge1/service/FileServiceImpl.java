package com.challenge1.service;

import com.challenge1.service.api.FileService;
import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;


@Service
public class FileServiceImpl implements FileService {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    Logger LOG = LoggerFactory.getLogger(this.getClass());


    @Override
    public Iterable<Node> getIteratorForPath(Path path) {
        Iterable<Node> nodeIterator = NodeLogic.getNodeIterator(new FileNodeImpl(path));
        return nodeIterator;
    }

}
