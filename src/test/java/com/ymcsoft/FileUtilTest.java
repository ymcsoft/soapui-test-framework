package com.ymcsoft;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

public class FileUtilTest {
    @Test
    public void testFilter() throws Exception {
        String testPath = "C:\\path\\myproject-soapui-project.xml";
        Assert.assertTrue(FileUtil.filter(testPath, Pattern.compile("\\.xml")));
    }

}