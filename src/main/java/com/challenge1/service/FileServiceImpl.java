package com.challenge1.service;

import com.challenge1.service.api.FileService;
import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Service
public class FileServiceImpl implements FileService {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    Map<String, List<String>> resultMap = new HashMap<>();


    @Override
    public Iterator<Node> getIteratorForPath(Path path) {
        FileNodeImpl fileNode = new FileNodeImpl(path);
        return null;
    }

    public String withMapSolution(Path path) throws IOException {
        innerMapCollect(path);
        StringBuilder bb = new StringBuilder();
        resultMap.forEach((s, strings) -> mapHandler(s, strings, bb));
        return bb.toString();
    }

    private void mapHandler(String s, List<String> strings, StringBuilder bb) {
        bb.append("FOLDER---> ").append(s).append(LINE_SEPARATOR);
        strings.stream().forEach(element -> bb.append(element).append(LINE_SEPARATOR));
        bb.append(LINE_SEPARATOR);
        bb.append(LINE_SEPARATOR);
    }

    private void innerMapCollect(Path path) {
        try {
            Files.newDirectoryStream(path).forEach(element -> addResultToMap(element));
        } catch (IOException e) {

        }
    }

    private void addResultToMap(Path path) {
        if (path.toFile().isDirectory()) {
            if (resultMap.get(path.toString()) != null) {
                resultMap.put(path.toString(), null);
            }
            innerMapCollect(path);
        } else {
            if(resultMap.get(path.getParent().toString()) == null){
                resultMap.put(path.getParent().toString(),Arrays.asList(path.getFileName().toString()));
            }else{
                List<String> strings = new ArrayList<>();
                resultMap.get(path.getParent().toString()).add(path.getFileName().toString());
                strings.add(path.getFileName().toString());
                resultMap.put(path.getParent().toString(),strings);
                }

        }
    }
}
