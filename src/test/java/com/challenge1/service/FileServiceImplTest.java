package com.challenge1.service;

import com.challenge1.service.api.FileService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;

/**
 * Created by mlgy on 2016-09-19.
 */
public class FileServiceImplTest {

    public static String TEST_RESOURCE_PATH = "src/test/resources/ParentFolder";

    @BeforeClass
    public static void  setUp() throws FileNotFoundException {
        System.out.print("PATH FOUND getLocation" + FileServiceImplTest.class.getProtectionDomain().getCodeSource().getLocation());
    }

    FileService fileService = new FileServiceImpl();

    @Test
    public void getCurrentFileStructure() {

        Iterator<File> fileIterator = fileService.collectFoldersForPath(".");
        StringBuilder sBuilder = new StringBuilder();
        while (fileIterator.hasNext()) {
            File next = fileIterator.next();
            sBuilder.append("Abs Path: " + next.getAbsolutePath());
            sBuilder.append(System.lineSeparator());
            sBuilder.append("Path: " + next.getPath());
            sBuilder.append(System.lineSeparator());

        }

        System.out.print("TESTPRINT " +sBuilder.toString());

        directoryOnlyCheck(fileIterator);
    }

    @Test
    public void shouldReturnOneFolderElementIterator() {

        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder1/EmptyFolder");
        Assert.assertNotNull(fileIterator);
        Assert.assertTrue(fileIterator.hasNext());

        directoryOnlyCheck(fileIterator);
    }


    @Test
    public void shouldReturnEmptyIteratorForNotExistingPath() {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(null);
        Assert.assertNotNull(fileIterator);
        Assert.assertFalse(fileIterator.hasNext());

    }

    @Test
    public void shouldReturnEmptyIteratorForBlankPath() {
        Iterator<File> fileIterator = fileService.collectFoldersForPath("");
        Assert.assertNotNull(fileIterator);
        Assert.assertFalse(fileIterator.hasNext());

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidPath() {
        fileService.collectFoldersForPath(".\\xyz\\someRandompath");
    }

    @Test
    public void shouldGetNestedFoldersWithNoFiles() {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH);
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator);

    }

    @Test
    public void shouldIgnoreFilesInFolder() {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder2");
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator);
    }

    @Test
    public void shouldIgnoreFileInNestedFolders() {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder1");
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator);
    }


    private void directoryOnlyCheck(Iterator<File> fileIterator) {
        while (fileIterator.hasNext()) {
            File next = fileIterator.next();
            Assert.assertThat(next.isDirectory(), is(true));
        }
    }
}