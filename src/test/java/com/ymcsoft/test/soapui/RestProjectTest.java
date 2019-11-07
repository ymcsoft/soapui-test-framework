package com.ymcsoft.test.soapui;

import com.eviware.soapui.tools.SoapUIMockServiceRunner;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RestProjectTest {

    private SoapApiTest fixture;

    @BeforeSuite
    public void setUp() throws Exception {
        URL resourceUrl = RestProjectTest.class.getClassLoader().getResource("projects/REST-test-soapui-project.xml");
        //run mock service before starting the test
        SoapUIMockServiceRunner mockRunner = new SoapUIMockServiceRunner();
        mockRunner.setProjectFile(resourceUrl.toString());
        mockRunner.run();
        fixture = new SoapApiTest(resourceUrl.toString());
    }

    @Test
    public void runAllTestsInSuite() throws Throwable{
        Map<String, String> requestParameters = new HashMap<String, String>();
        fixture.runAllTestsInSuites(requestParameters);
    }
}
