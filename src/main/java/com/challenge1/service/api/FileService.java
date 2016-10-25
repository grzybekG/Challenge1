package com.challenge1.service.api;

import rx.Observable;

import java.nio.file.Path;

public interface FileService {

    Observable<Node<Path>> getObservableFor(Path path);

}
