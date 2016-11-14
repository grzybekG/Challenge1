package com.challenge1.web;

import com.challenge1.service.api.ObserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class DirectoryController {
    @Autowired
    private ObserverService fileService;

    @RequestMapping(path = "/other", method = RequestMethod.GET)
    public String testController(StompHeaderAccessor accessor) throws IOException {

        //FIXME Return stream
        //fileService.getObservableForPath(Paths.get(param));
        return " abc";
    }

}

