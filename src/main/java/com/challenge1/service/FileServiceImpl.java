package com.challenge1.service;

import com.challenge1.service.api.FileService;
import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

import java.nio.file.Path;


@Service
public class FileServiceImpl implements FileService {
    Logger LOG = LoggerFactory.getLogger(this.getClass());


    @Override
    public Observable<Node<Path>> getObservableFor(Path path) {
        Iterable<Node<Path>> nodeIterator = NodeLogic.getNodeIterator(new FileNodeImpl(path));
        Observable<Node<Path>> feed = ObservableServiceImpl.getObservable(nodeIterator);

        LOG.info("Getting iterator for path {}.", path);


        Subscription imDone = feed.subscribe(new Subscriber<Node<Path>>() {
            @Override
            public void onCompleted() {
                LOG.info("imDone");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Sth BAD happend... stack ->" + throwable);
                unsubscribe();
            }

            @Override
            public void onNext(Node<Path> node) {
                LOG.info("HHH" +node.getData());
            }
        });

        return null;
    }


}
