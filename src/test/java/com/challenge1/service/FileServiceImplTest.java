package com.challenge1.service;

import com.challenge1.service.api.FileService;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;

/**
 * Created by mlgy on 2016-09-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FileServiceImplTest {

    public static String TEST_RESOURCE_PATH = ".\\ParentFolder";

    FileService fileService = new FileServiceImpl();

    @BeforeClass
    public static void setup() throws IOException {
        boolean mkdirs = new File(".\\ParentFolder\\SubFolder1\\EmptyFolder").mkdirs();
        new File(".\\ParentFolder\\SubFolder2\\SubSubFolderA").mkdirs();
        new File(".\\ParentFolder\\SubFolder2\\SubSubFolderB").mkdirs();
        new File(".\\ParentFolder\\SubFolder2\\SubSubFolderB\\NestedOne").mkdirs();
        byte data[] = {1,2};
        Path someFile = Paths.get(TEST_RESOURCE_PATH +"\\someFile");
        Path someSubFile1 = Paths.get(TEST_RESOURCE_PATH +"\\SubFolder1\\someSubFile1");
        Path someSubFile2 = Paths.get(TEST_RESOURCE_PATH +"\\SubFolder2\\someSubFile2");
        Files.write(someFile, data);
        Files.write(someSubFile1, data);
        Files.write(someSubFile2, data);

        System.out.print("Folder has been created :[" +mkdirs +"]"+System.lineSeparator());
        System.out.print("Path to parent folder is A: [" +new File("ParentFolder").getPath()) ;
    }
    @AfterClass
    public static void destroy() throws Exception{
        FileUtils.deleteDirectory(new File(TEST_RESOURCE_PATH));


    }
    @Test
    public void shouldReturnOneFolderElementIterator() throws Exception{
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "\\SubFolder1\\EmptyFolder");
        Assert.assertNotNull(fileIterator);
        Assert.assertTrue(fileIterator.hasNext());

        directoryOnlyCheck(fileIterator, 1);
    }


    @Test
    public void shouldReturnEmptyIteratorForNotExistingPath() throws Exception{
        Iterator<File> fileIterator = fileService.collectFoldersForPath(null);
        Assert.assertNotNull(fileIterator);
        Assert.assertFalse(fileIterator.hasNext());

    }

    @Test
    public void shouldReturnEmptyIteratorForBlankPath() throws Exception{
        Iterator<File> fileIterator = fileService.collectFoldersForPath("");
        Assert.assertNotNull(fileIterator);
        Assert.assertFalse(fileIterator.hasNext());

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidPath() {
        fileService.collectFoldersForPath(".\\xyz\\someRandompath");
    }

    @Test
    public void shouldGetNestedFoldersWithNoFiles() throws Exception{
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH);
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator,7);

    }

    @Test
    public void shouldIgnoreFilesInFolder() throws Exception{
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder2");
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator,4);
    }

    @Test
    public void shouldIgnoreFileInNestedFolders() throws Exception{
        Iterator<File> fileIterator = fileService.collectFoldersForPath(TEST_RESOURCE_PATH + "/SubFolder1");
        Assert.assertNotNull(fileIterator);
        directoryOnlyCheck(fileIterator,2);
    }


    private void directoryOnlyCheck(Iterator<File> fileIterator,int expectedSize) {
        int count=0;
        while (fileIterator.hasNext()) {
            File next = fileIterator.next();
            Assert.assertThat(next.isDirectory(), is(true));
            count++;
        }
        Assert.assertThat(count,is(expectedSize));
    }
}