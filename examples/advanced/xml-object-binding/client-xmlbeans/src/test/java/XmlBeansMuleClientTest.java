import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import jp.co.ogisRi.sampleCOMMAND.*;
import jp.co.ogisRi.sampleRESPONSE.*;

/**
 * Unit test for simple XmlBeansMuleClient.
 */
public class XmlBeansMuleClientTest extends TestCase {

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public XmlBeansMuleClientTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite(XmlBeansMuleClientTest.class);
  }

  /**
   * Rigorous Test :-)
   */
  public void testXmlBeansMuleClient1() 
  {
    try {

      SampleCommandDocument sampleCommandDocument 
      = SampleCommandDocument.Factory.newInstance();
      SampleCommandDocument.SampleCommand sampleCommand = sampleCommandDocument.addNewSampleCommand();
      SampleCommandDocument.SampleCommand.CustomerInfo customer = sampleCommand.addNewCustomerInfo();
      SampleCommandDocument.SampleCommand.CustomerInfo.CustomerNo no = customer.addNewCustomerNo();
      SampleCommandDocument.SampleCommand.CustomerInfo.CustomerName name = customer.addNewCustomerName();
      SampleCommandDocument.SampleCommand.CustomerInfo.CustomerAddress address = customer.addNewCustomerAddress();
      SampleCommandDocument.SampleCommand.CustomerInfo.SearchDate searchDate = customer.addNewSearchDate();
      no.setStringValue("1111");
      name.setStringValue("‚¢‚¢‚¢‚¢‚¢‚¢‚¢");
      address.setStringValue("‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ");
      searchDate.setStringValue("20060701");
      
      XmlBeansBindingProvider binder = new XmlBeansBindingProvider();

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
      Object o = binder.xmlToJavaObject(responseXML, SampleResponseDocument.class);    
      if (o == null || !(o instanceof SampleResponseDocument)) {
          throw new Exception("No response from server. Please make sure that the server is started.");
      }
      SampleResponseDocument response = (SampleResponseDocument) o;
      

      SampleResponseDocument.SampleResponse sampleResponse = response.getSampleResponse();
      SampleResponseDocument.SampleResponse.CustomerList[] customers = sampleResponse.getCustomerListArray();

      for (int i=0; i<customers.length; i++) {

          String cust_no      = customers[i].getCustomerNo().getStringValue();
          String cust_name    = customers[i].getCustomerName().getStringValue();
          String cust_address = customers[i].getCustomerAddress().getStringValue();
          String cust_amount  = customers[i].getCustomerAmount().getStringValue();
          String cust_charge  = customers[i].getCustomerCharge().getStringValue();
          System.out.println("Customer no = "      + cust_no);
          System.out.println("Customer name = "    + cust_name);
          System.out.println("Customer address = " + cust_address);
          System.out.println("Customer amount = "  + cust_amount);
          System.out.println("Customer charge = "  + cust_charge);
          System.out.println("");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
