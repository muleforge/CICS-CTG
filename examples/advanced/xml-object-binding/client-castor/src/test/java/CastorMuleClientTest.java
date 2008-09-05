import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import jp.co.ogis.sample.castor.*;

/**
 * Unit test for simple CastorMuleClient.
 */
public class CastorMuleClientTest extends TestCase {

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public CastorMuleClientTest( String testName ) {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite( CastorMuleClientTest.class );
  }

  /**
   * Rigourous Test :-)
   */
  public void testCastorMuleClient1()
  {
    try {
      SampleCommand sampleCommand = new SampleCommand();
      CustomerInfo customer = new CustomerInfo();

      CustomerNo no = new CustomerNo();
      no.setContent("111");
      customer.setCustomerNo(no);

      CustomerName name = new CustomerName();
      name.setContent("‚¢‚¢‚¢‚¢‚¢‚¢‚¢");
      customer.setCustomerName(name);

      CustomerAddress address = new CustomerAddress();
      address.setContent("‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ");
      customer.setCustomerAddress(address);

      SearchDate searchDate = new SearchDate();
      searchDate.setContent("20060701");
      customer.setSearchDate(searchDate);

      sampleCommand.setCustomerInfo(customer);
      
      CastorBindingProvider binder = new CastorBindingProvider();
      
      // Convert java to XML
      String requestXML = binder.javaObjectToXml(sampleCommand);

      // Add request header to XML
      requestXML = HeaderTransformer.addRequestHeader("sample-command", requestXML);
      
      // Create mule-client instance
      MuleClientProxy muleClient = new MuleClientProxy("mule-config.xml");

      // Send request to mule server
      String responseXML = muleClient.requestSend("vm://endpoint1", requestXML);
      
      // Remove header from XML
      responseXML = HeaderTransformer.removeResponseHeader("sample-response", responseXML);
      
      // Convert XML to java object
      SampleResponse response = (SampleResponse) binder.xmlToJavaObject(responseXML,  SampleResponse.class);    

      CustomerList[] customers = response.getCustomerList();
      for (int i=0; i<customers.length; i++) {
          CustomerNo custno           = customers[i].getCustomerNo();
          CustomerName custname       = customers[i].getCustomerName();
          CustomerAddress custaddress = customers[i].getCustomerAddress();
          CustomerAmount custamount   = customers[i].getCustomerAmount();
          CustomerCharge custcharge   = customers[i].getCustomerCharge();

          System.out.println("Customer no = " + custno.getContent());
          System.out.println("Customer name = " + custname.getContent());
          System.out.println("Customer address = " + custaddress.getContent());
          //System.out.println("Customer amount = " + custamount.getContent());
          //System.out.println("Customer charge = " + custcharge.getContent());
          System.out.println("");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
