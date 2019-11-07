package com.ymcsoft.test.soapui;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestRunner.Status;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.settings.HttpSettings;
import org.apache.log4j.Logger;

import java.net.ProxySelector;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SoapUiProject handles test runs and property management
 * <p>
 * @author Yuri Moiseyenko
 * Created on 6/13/2017.
 */
public class SoapUiProject {
    private static final Logger log = Logger.getLogger(SoapUiProject.class.getName());

    private WsdlProject project;

    public SoapUiProject(String projectPath) {

        // SoapUI sets to null proxy selector which will make it not accessible
        ProxySelector proxy = ProxySelector.getDefault();

        SoapUI.getSettings().setString(HttpSettings.RESPONSE_COMPRESSION, "gzip");
        try {
            project = new WsdlProject(projectPath);
            ProxySelector.setDefault(proxy);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage(), e);
        }
    }

    /**
     * Runs SoapUI test
     * @param testSuiteName String  SoapUI TestSuite name
     * @param testCaseName String SoapUI TestCase name
     * @param map String optional TestCase parameters
     * @return Status test case status (ex.: FINISHED, FAILED)
     */
    public Status runTest(String testSuiteName, String testCaseName, Map<String, String> map) {

        TestSuite testSuite = project.getTestSuiteByName(testSuiteName);
        TestCase testCase = testSuite.getTestCaseByName(testCaseName);

        for (String propertyName : map.keySet()) {
            testCase.setPropertyValue(propertyName, map.get(propertyName));
        }
        // setting logging
        //testCase.setPropertyValue("soapui.log4j.config", "log4j.xml");
        TestRunner runner = testCase.run(new PropertiesMap(), false);
        return runner.getStatus();
    }

    /**
     * Runs SoapUI test
     * @param testSuiteName String  SoapUI TestSuite name
     * @param testCaseName String SoapUI TestCase name
     * @return Status test case status (ex.: FINISHED, FAILED)
     */
    public Status runTest(String testSuiteName, String testCaseName) {
        Map<String, String> requestParameters = new HashMap<String, String>();
        return runTest(testSuiteName, testCaseName, requestParameters);
    }

    /**
     * Get property value of the SoapUI TestCase
     * @param testSuiteName String  SoapUI TestSuite name
     * @param testCaseName String SoapUI TestCase name
     * @param propertyName String SoapUI TestCase property name
     * @return String value of the property
     */
    public String getTestCaseProperty(String testSuiteName, String testCaseName, String propertyName) {
        TestSuite testSuite = project.getTestSuiteByName(testSuiteName);
        TestCase testCase = testSuite.getTestCaseByName(testCaseName);
        return testCase.getPropertyValue(propertyName);
    }


    /**
     * Get property value of the SoapUI TestSuite
     * @param testSuiteName String  SoapUI TestSuite name
     * @param propertyName String SoapUI TestCase property name
     * @return String value of the property
     */
    public String getTestSuiteProperty(String testSuiteName, String propertyName) {
        TestSuite testSuite = project.getTestSuiteByName(testSuiteName);
        return testSuite.getPropertyValue(propertyName);
    }

    /**
     * Set property value of the SoapUI TestSuite
     * @param testSuiteName String  SoapUI TestSuite name
     * @param propertyName String SoapUI TestSuite property name
     * @param propertyValue String value of the property
     */
    public void setTestSuiteProperty(String testSuiteName, String propertyName, String propertyValue) {
        TestSuite testSuite = project.getTestSuiteByName(testSuiteName);
        testSuite.setPropertyValue(propertyName, propertyValue);
    }

    /**
     * Set property value of the SoapUI TestCase
     * @param testSuiteName String  SoapUI TestSuite name
     * @param testCaseName String SoapUI TestCase name
     * @param testCasePropertyName String SoapUI TestCase property name
     * @param testCasePropertyValue String value of the property
     */
    public void setTestCaseProperty(String testSuiteName, String testCaseName, String testCasePropertyName, String testCasePropertyValue) {
        TestSuite testSuite = project.getTestSuiteByName(testSuiteName);
        TestCase testcase = testSuite.getTestCaseByName(testCaseName);
        testcase.setPropertyValue(testCasePropertyName, testCasePropertyValue);
    }

    /**
     * Set SoapUI TestProject property value
     * @param propertyName String SoapUI TestProject property name
     * @param propertyValue String property value
     */
    public void setTestProjectProperty(String propertyName, String propertyValue) {
        project.setPropertyValue(propertyName, propertyValue);
    }

    /**
     * Run SoapUI TestSuite by name
     * @param testSuiteName String SoapUI TestSuite name
     * @param map optional SoapUI TestSuite parameters
     * @return
     */
    public Status runSuite(String testSuiteName, Map<String, String> map) {
        log.info("Test Suite " + testSuiteName + " is starting...");
        TestSuite testSuite = project.getTestSuiteByName(testSuiteName);
        for (String propertyName : map.keySet()) {
            testSuite.setPropertyValue(propertyName, map.get(propertyName));
        }
        // setting logging
        //testCase.setPropertyValue("soapui.log4j.config", "log4j.xml");
        TestRunner runner = testSuite.run(new PropertiesMap(), false);
        Status status = runner.getStatus();
        log.info("Test Suite " + testSuiteName + " finished.");
        return status;
    }

    /**
     * Run all SoapUI TestSuites
     * @param map optional SoapUI TestSuite parameters
     * @return map of SoapUI TestSuite name and Status
     */
    public Map<String, Status> runAllSuites(Map<String, String> map) {
        Map<String, Status> result = new LinkedHashMap<>();
        for (TestSuite testSuite : project.getTestSuiteList()) {
            log.info("Test Suite " + testSuite.getName() + " is starting...");
            Status status = runSuite(testSuite.getName(), map);
            result.put(testSuite.getName(), status);
            log.info("Test Suite " + testSuite.getName() + " finished.");
        }
        return result;
    }

    /**
     * List all available test suites in the SoapUI project
     * @return list of SoapUI TestSuite objects
     */
    public List<TestSuite> getAllTestSuites() {
        return project.getTestSuiteList();
    }
}
