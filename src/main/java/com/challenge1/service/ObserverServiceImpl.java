package com.challenge1.service;

import com.challenge1.service.api.ObserverService;
import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

import java.nio.file.Path;


@Service
public class ObserverServiceImpl implements ObserverService {

    @Override
    public Observable<Node<?>> getObservableForPath(Path path) {
        return Observable.from(NodeLogic.getNodeIterator(new FileHandlerImpl(path)));
    }


}
