package com.challenge1.web;

import com.challenge1.service.ObserverServiceImpl;
import com.challenge1.service.api.ObservableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class DirectoryController {
    @Autowired
    ObservableService service;

    @RequestMapping(path = "/other", method = RequestMethod.GET)
    public String testController() throws IOException {

     return "not implemented at all";
    }

}

