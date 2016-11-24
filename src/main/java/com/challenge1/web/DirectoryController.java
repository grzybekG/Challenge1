package com.challenge1.web;

import com.challenge1.service.ObserverServiceImpl;
import com.challenge1.service.PathContext;
import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class DirectoryController {
    @Autowired
    ObservableService service;
    @Autowired
    SimpMessagingTemplate template;

    @RequestMapping(path = "/other", method = RequestMethod.GET)
    public String testController() throws IOException {

        return "not implemented at all";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public SimpleResponse greeting(Message<String> message) throws Exception {

        Thread.sleep(500); // simulated delay


        return new SimpleResponse(message.getPayload());
    }


    @MessageMapping("/init")
    public void nothing(StompHeaderAccessor headerAccessor){
        String sessionId = headerAccessor.getSessionId();
        final SimpMessageHeaderAccessor outgoingHeader = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);


        Observable<PathContext> directoryWatcherObservable = service.getDirectoryWatcherObservable(Paths.get("c:\\root"));
        Observable<PathContext> observableForPath = service.getObservableForPath(Paths.get("c:\\root"));
        final Observable<PathContext> sum = Observable.concat(directoryWatcherObservable, observableForPath);


        sum.subscribeOn(Schedulers.io()).subscribe(subscriber -> {

            template.convertAndSendToUser(sessionId, "/topic/greetings/", new SimpleResponse(subscriber.getPath().toString()),outgoingHeader.getMessageHeaders());

        });

    }
}

