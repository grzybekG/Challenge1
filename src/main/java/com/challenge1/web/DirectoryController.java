package com.challenge1.web;

import com.challenge1.service.api.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Iterator;

@RestController
public class DirectoryController {
    @Autowired
    private FileService fileService;

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String testController() {
        StringBuilder sBuilder = new StringBuilder();
        Iterator<File> fileIterator = fileService.collectFoldersForPath(".");
        while (fileIterator.hasNext()) {
            File next = fileIterator.next();
            sBuilder.append("Name: " + next.getName());
            sBuilder.append("Abs Path: " + next.getAbsolutePath());
            sBuilder.append("Is File: " + next.isFile());
            sBuilder.append("Is Directory: " + next.isDirectory());
            sBuilder.append(System.lineSeparator());

        }

        return sBuilder.toString();

    }
}

