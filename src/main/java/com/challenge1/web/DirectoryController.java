package com.challenge1.web;

import com.challenge1.service.api.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class DirectoryController {
    @Autowired
    private FileService fileService;

    @RequestMapping(path = "/other", method = RequestMethod.GET)
    public String testController(@RequestParam String param) throws IOException {
        //FIXME

        return fileService.getIteratorForPath(Paths.get(param)).toString();
    }

}

