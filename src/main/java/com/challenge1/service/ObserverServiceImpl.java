package com.challenge1.service;

import com.challenge1.service.api.FileModificationListener;
import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import com.challenge1.service.watcher.DirectoryRegistration;
import com.challenge1.service.watcher.StructureWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;
import rx.internal.operators.OperatorSubscribeOn;

import java.nio.file.Path;


@Service
public class ObserverServiceImpl implements ObservableService {
Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Override
    public Observable<Node<?>> getObservableForPath(Path path) {
        return Observable.from(NodeLogic.getNodeIterator(new FileHandlerImpl(path)));
    }

    @Override
    public Observable<Node<?>> getDirectoryWatcherObservable(Path path) {

        return Observable.create(subscriber -> {


            StructureWatcher watcher = new StructureWatcher(new DirectoryRegistration(path, new FileModificationListener() {
                @Override
                public void onAction(Node<?> node) {
                    LOG.info("Node from observer listener emmited. For Path [{}]", node.getData().toString());
                    subscriber.onNext(node);
                }
            }));
            Thread t = new Thread(watcher);
            t.start();
        });
    }
}
