package com.challenge1.web;

import com.challenge1.service.api.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class DirectoryController {
    @Autowired
    private FileService fileService;

    @RequestMapping(path = "/other", method = RequestMethod.GET)
    public String testController(@RequestParam String param) throws IOException {
        //FIXME

        return fileService.getObservableFor(Paths.get(param)).toString();
    }

}

