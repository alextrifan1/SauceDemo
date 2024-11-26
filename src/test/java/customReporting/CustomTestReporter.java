package customReporting;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class CustomTestReporter implements IReporter {
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        PrintWriter reportWriter = null;
        File reportFile = new File("target/customReport_" + Instant.now().getEpochSecond() + ".html");
        try {
            reportWriter = new PrintWriter(new BufferedWriter(new FileWriter(reportFile)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (reportWriter == null) {
            return ;
        }
        reportWriter.println("<!DOCTYPE html>");
        reportWriter.println("<html>");
        reportWriter.println("<head>");
        reportWriter.println("<title>My Custom Report </title>");
        reportWriter.println("</head>");
        reportWriter.println("<body>");
        for (ISuite suite: suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            reportWriter.println("<p>"  + suite.getName());
            reportWriter.println("<ul>");
            for (String key : result.keySet()) {
                ISuiteResult r = result.get(key);
                reportWriter.println("<li> " + r.getTestContext().getName() + "</li>");
                reportWriter.println("<li> Failed Tests: " + r.getTestContext().getFailedTests().getAllResults().size() + "</li>");
                reportWriter.println("<li> Passed Tests: " + r.getTestContext().getPassedTests().getAllResults().size() + "</li>");
                reportWriter.println("<br />");
            }
            reportWriter.println("</ul></p>");
        }
        reportWriter.println("</body>");
        reportWriter.println("</html>");
        reportWriter.flush();
        reportWriter.close();
    }
}
