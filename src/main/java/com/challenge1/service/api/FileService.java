package com.challenge1.service.api;

import java.io.File;
import java.util.Iterator;

/**
 * Created by mlgy on 2016-09-19.
 */
public interface FileService {
    /**
     * Collect folders under given path, will travers in search of nested folders and ignore files
     * @param path root path from which it will start
     * @return Iterator over folder element, if path is null/empty will give empty iterator. When path is not a directory will throw IllegalArgumentException
     */
    Iterator<File> collectFoldersForPath(String path);

}
