package com.challenge1.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryController {

    @RequestMapping(path = "/test", method = RequestMethod.GET)
        public String testController(){
            return "hello world";
        }

    }

