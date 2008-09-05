import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextFactory;
import org.mule.config.builders.SimpleConfigurationBuilder;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.module.client.MuleClient;
import org.mule.util.IOUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple CastorMuleClient.
 */
public class MuleClientTest extends TestCase {

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public MuleClientTest(String testName) {
    super(testName);

    try {
        MuleContext context = createMuleContext("mule-config.xml");
        context.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
  
  private MuleContext createMuleContext(String configFile) throws Exception {
      List builders = new ArrayList();
      builders.add(new SimpleConfigurationBuilder(null));
      builders.add(new SpringXmlConfigurationBuilder(configFile));
      MuleContextFactory ctxFactory = new DefaultMuleContextFactory();
      return ctxFactory.createMuleContext(builders, new DefaultMuleContextBuilder());
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
      return new TestSuite(MuleClientTest.class);
  }
  
  /**
   * Rigourous Test :-)
   */
  public void testMuleCicsClient()
  {
    System.out.println("In testMuleCicsClient()");
    try {
      System.out.println("Reading sample-command-request.xml...");

      // Read request XML from resources
      String requestXML = IOUtils.getResourceAsString("sample-command-request.xml", this.getClass());
      
      System.out.println("Creating instance of MuleClient..");

      // Create mule-client instance
      MuleClient muleClient = new MuleClient();
      System.out.println("Sending XML to vm://endpoint1");

      // Send request to mule server
      MuleMessage result = muleClient.send("vm://endpoint1", requestXML, null);
      String responseXML = result.getPayloadAsString();
      System.out.println("--------------------Response XML---------------");
      System.out.println(responseXML);
      System.out.println("-----------------------------------------------");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
