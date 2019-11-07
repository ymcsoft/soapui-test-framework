package com.ymcsoft.test.soapui;

import com.eviware.soapui.model.testsuite.TestRunner;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

/**
 * SoapApiTest class runs tests defined in SoapUI project
 * <p>
 * @author Yuri Moiseyenko
 * Created on 6/13/2017.
 */
public class SoapApiTest {
	private static final Logger logger = Logger.getLogger(SoapApiTest.class.getName());
	protected static final String DEFAULT_PROJECT_PATH;

	protected final String testSuiteName;
	protected final String projectPath;
	protected final String projectFileName;
	protected final SoapUiProject soapUiProject;

	static {
		DEFAULT_PROJECT_PATH = System.getProperty("soaui.project.path");
	}

	public SoapApiTest(String projectFileName) {
		this(DEFAULT_PROJECT_PATH, projectFileName, null);
	}

	public SoapApiTest(String projectName, String testSuiteName) {
		this(DEFAULT_PROJECT_PATH, projectName, testSuiteName);
	}

	public SoapApiTest(String projectPath, String projectName, String testSuiteName) {
		this.projectPath = projectPath;
		if(this.projectPath != null) {
			this.projectFileName = this.projectPath + File.separator + projectName;
		}
		else {
			this.projectFileName = projectName;
		}
		this.testSuiteName = testSuiteName;
		soapUiProject = new SoapUiProject(projectFileName);
	}

	public SoapApiTest(URL projectUrl, String testSuiteName) {
		this.projectPath = DEFAULT_PROJECT_PATH;
		this.projectFileName = projectUrl.toString();
		this.testSuiteName = testSuiteName;
		soapUiProject = new SoapUiProject(projectFileName);
	}

	@BeforeMethod
	public void handleTestMethodName(Method method) {
		logger.info("==========================================================");
		logger.info("Running test:" + method.getName() + " ... ");
		logger.info("==========================================================");
	}

	public void runAllTestsInSuite(Map<String,String> parameters) throws Throwable {
		try {
			logger.info("Test starting...");
			TestRunner.Status status = soapUiProject.runSuite(testSuiteName, parameters);
			Assert.assertEquals(TestRunner.Status.FINISHED, status);
		} catch (Exception | Error exc) {
			logger.log(Level.ERROR,"Failed to run " + testSuiteName, exc);
			throw exc;
		}
	}

	public void runAllTestsInSuite(String testSuiteName, Map<String,String> parameters) throws Throwable{
		try {
			logger.info("Test starting...");
			TestRunner.Status status = soapUiProject.runSuite(testSuiteName, parameters);
			logger.info("Test finished.");
			Assert.assertEquals(TestRunner.Status.FINISHED, status);
		} catch (Exception | Error e) {
			logger.log(Level.ERROR,"Failed to run " + testSuiteName, e);
			throw e;
		}
	}

	public void runAllTestsInSuites(Map<String, String> parameters) throws  Throwable{
		String failedSuiteName = null;
		try {
			logger.info("Test starting...");
			Map<String, TestRunner.Status> results = soapUiProject.runAllSuites(parameters);
			for (String suiteName : results.keySet()) {
				failedSuiteName = suiteName;
				Assert.assertEquals(TestRunner.Status.FINISHED, results.get(suiteName));
			}
			logger.info("Test finished.");
		} catch (Exception | Error e) {
			logger.log(Level.ERROR,"Failed to run " + failedSuiteName, e);
			throw e;
		}
	}
}
