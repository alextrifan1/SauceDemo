package customReporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.time.Instant;

public class CustomTestListener extends TestListenerAdapter {
    ExtentReports extent = new ExtentReports();
    @Override
    public void onStart(ITestContext context) {
        long unixTimestamp = Instant.now().getEpochSecond();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/report_"+ unixTimestamp + ".html");
        extent.attachReporter(sparkReporter);
    }
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
    @Override
    public void onTestStart(ITestResult result) {
        String testMethodName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        System.out.println("Start Test: " + testMethodName);
        ExtentTest mytest = extent.createTest(testMethodName);
        mytest.log(Status.INFO,"Test started: " + testMethodName);
        mytest.log(Status.INFO,"Test description: " + testDescription);
    }
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest mytest = extent.createTest(result.getMethod().getMethodName());
        mytest.pass("Test passed");
    }
    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest mytest = extent.createTest(result.getMethod().getMethodName());
        mytest.fail("Test failed");
    }
}