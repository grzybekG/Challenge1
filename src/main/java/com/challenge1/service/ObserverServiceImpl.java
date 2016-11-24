package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import com.challenge1.service.api.Type;
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
    public Observable<PathContext> getObservableForPath(Path path) {
        return Observable.from(nodeLogic.getNodeIterator(new FileHandlerImpl(path))).map(pathNode -> new PathContext(pathNode.getData(), Type.NEW_ENTRY));
    }

    @Override
    public Observable<PathContext> getDirectoryWatcherObservable(Path path) {
        return Observable.create(subscriber -> {
            DirectoryRegistration directoryRegistration = new DirectoryRegistration(WatchServiceFactory.getWatchService(), nodeLogic, context -> {
                LOG.info("Node from observer listener emitted. For Path [{}] of Type [{}]", context.getPath(), context.getType());
                subscriber.onNext(context);
            });
            directoryRegistration.init(path);
            final StructureWatcher watcher = new StructureWatcher(directoryRegistration);
            watcher.processEvents();
        });
    }
}
