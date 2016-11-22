package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import com.challenge1.service.watcher.DirectoryRegistration;
import com.challenge1.service.watcher.StructureWatcher;
import com.challenge1.service.watcher.WatchServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.nio.file.Path;

@Service
public class ObserverServiceImpl implements ObservableService {
    private static final Logger LOG = LoggerFactory.getLogger(ObserverServiceImpl.class);
    private NodeLogic nodeLogic;

    @Autowired
    public ObserverServiceImpl(NodeLogic nodeLogic) {
        this.nodeLogic = nodeLogic;
    }

    @Override
    public Observable<Node<?>> getObservableForPath(Path path) {
        return Observable.from(nodeLogic.getNodeIterator(new FileHandlerImpl(path)));
    }

    @Override
    public Observable<Node<?>> getDirectoryWatcherObservable(Path path) {
        return Observable.create(subscriber -> {
            DirectoryRegistration directoryRegistration = new DirectoryRegistration(WatchServiceFactory.getWatchService(), nodeLogic, node -> {
                LOG.info("Node from observer listener emitted. For Path [{}] of Type [{}]", node.getData().toString(), node.getType());
                subscriber.onNext(node);
            });
            directoryRegistration.init(path);
            StructureWatcher watcher = new StructureWatcher(directoryRegistration);
            Thread t = new Thread(watcher);
            t.start();
        });
    }
}
