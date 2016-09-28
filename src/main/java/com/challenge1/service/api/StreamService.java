package com.challenge1.service.api;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by mlgy on 2016-09-27.
 */
public interface StreamService {
    DirectoryStream getDirectoryStream(Path path);
}
