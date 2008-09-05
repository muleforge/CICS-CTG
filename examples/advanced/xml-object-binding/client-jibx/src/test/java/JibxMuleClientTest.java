import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple JibxMuleClient.
 */
public class JibxMuleClientTest extends TestCase {
  
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public JibxMuleClientTest( String testName ) {
	super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
	return new TestSuite( JibxMuleClientTest.class );
  }

  /**
   * Rigourous Test :-)
   */
  public void testJibxMuleClient1()
  {
    try {
	  SampleCommand sampleCommand = new SampleCommand();
	  Customer customer = new Customer();
	  customer.setNo("111");
	  customer.setName("‚¢‚¢‚¢‚¢‚¢‚¢‚¢");
	  customer.setAddress("‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ");
	  customer.setSearchDate("20060701");
	  sampleCommand.setCustomer(customer);

	  // Get the Binding Provider Impl
	  JibxBindingProvider binder = new JibxBindingProvider();

	  // Convert java to XML
	  String requestXML = binder.javaObjectToXml(sampleCommand);

	  // Add request header to XML
	  requestXML = HeaderTransformer.addRequestHeader("sample-command", requestXML);

	  // Create mule-client instance
	  MuleClientProxy muleClient = new MuleClientProxy("mule-config.xml");

	  // Send request to mule server
	  String responseXML = muleClient.requestSend("vm://endpoint1", requestXML);

	  // Remove header from response XML
	  responseXML = HeaderTransformer.removeResponseHeader("sample-response", responseXML);

	  // Convert XML to java object
	  SampleResponse response = (SampleResponse) binder.xmlToJavaObject(responseXML, SampleResponse.class);

	  List customers = response.getCustomerList();
	  for (int i=0; i<customers.size(); i++) {
	      customer = (Customer) customers.get(i);
	      System.out.println("Customer no = " + customer.getNo());
	      System.out.println("Customer name = " + customer.getName());
	      System.out.println("Customer address = " + customer.getAddress());
	      System.out.println("Customer searchDate = " + customer.getSearchDate());
	      System.out.println("Customer amount = " + customer.getAmount());
	      System.out.println("Customer charge = " + customer.getCharge());
	      System.out.println("");
	  }
    } catch (Exception e) {
	  e.printStackTrace();
    }
  }
}
