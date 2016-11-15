package com.challenge1.web;

import com.challenge1.service.FileHandlerImpl;
import com.challenge1.service.NodeLogic;
import com.challenge1.service.api.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DirectoryController {


    @RequestMapping(path = "/other", method = RequestMethod.GET)
    public String testController(@RequestParam String param) throws IOException {
     return "not implemented at all";
    }

}

