package com.challenge1.service;

import com.challenge1.service.api.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by mlgy on 2016-09-19.
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public Iterator<File> collectFoldersForPath(String path) {
        if (StringUtils.isNotEmpty(path)) {
            return FileUtils.iterateFilesAndDirs(new File(path), FalseFileFilter.INSTANCE, DirectoryFileFilter.INSTANCE);
        }
        return Collections.emptyIterator();

    }
}
