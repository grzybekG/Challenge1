package com.challenge1.web;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class DirectoryController {

    @RequestMapping(path = "/other", method = RequestMethod.GET)
    public String testController(@RequestParam String param) throws IOException {
     return "not implemented at all";
    }

}

