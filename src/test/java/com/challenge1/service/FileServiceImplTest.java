package com.challenge1.service;

import com.challenge1.service.api.FileService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;

/**
 * Created by mlgy on 2016-09-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FileServiceImplTest {


    public static String TEST_RESOURCE_PATH = "src/test/resources/ParentFolder";

    FileService fileService = new FileServiceImpl();

    @BeforeClass
    public static void setUp(){
        TEST_RESOURCE_PATH = new File(".").getAbsolutePath() + "/" + TEST_RESOURCE_PATH;
        System.out.print("TEST RESOURCE PATH: [" +TEST_RESOURCE_PATH +"].");
    }



    @Test
    public void shouldReturnOneFolderElementIterator() throws Exception {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder1/EmptyFolder");
        Assert.assertNotNull(fileIterator);
        Assert.assertTrue(fileIterator.hasNext());

        directoryOnlyCheck(fileIterator, 1);
    }


    @Test
    public void shouldReturnEmptyIteratorForNotExistingPath() throws Exception {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(null);
        Assert.assertNotNull(fileIterator);
        Assert.assertFalse(fileIterator.hasNext());

    }

    @Test
    public void shouldReturnEmptyIteratorForBlankPath() throws Exception {
        Iterator<File> fileIterator = fileService.collectFoldersForPath("");
        Assert.assertNotNull(fileIterator);
        Assert.assertFalse(fileIterator.hasNext());

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidPath() {
        fileService.collectFoldersForPath("./xyz/someRandompath");
    }

    @Test
    public void shouldGetNestedFoldersWithNoFiles() throws Exception {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH);
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator, 5);

    }

    @Test
    public void shouldIgnoreFilesInFolder() throws Exception {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder2");
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator, 2);
    }

    @Test
    public void shouldIgnoreFileInNestedFolders() throws Exception {
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder1");
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator, 2);
    }


    private void directoryOnlyCheck(Iterator<File> fileIterator, int expectedSize) {
        int count = 0;
        while (fileIterator.hasNext()) {
            File next = fileIterator.next();
            Assert.assertThat(next.isDirectory(), is(true));
            count++;
        }
        Assert.assertThat(count, is(expectedSize));
    }
}