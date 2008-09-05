import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import jp.co.ogis.sample.jaxp.*;

/**
 * Unit test for simple JaxbMuleClient.
 */
public class JaxbMuleClientTest extends TestCase {

    /**
     * Create the test case
     * 
     * @param testName name of the test case
     */
    public JaxbMuleClientTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(JaxbMuleClientTest.class);
    }

    /**
     * Rigorous Test :-)
     */
    public void testJaxbMuleClient1() {
    try {
        ObjectFactory requestFactory = new ObjectFactory();

        SampleCommand sampleCommand = requestFactory.createSampleCommand();
        SampleCommand.CustomerInfo customer = requestFactory
            .createSampleCommandCustomerInfo();
        sampleCommand.setCustomerInfo(customer);

        SampleCommand.CustomerInfo.CustomerNo no = requestFactory
            .createSampleCommandCustomerInfoCustomerNo();
        customer.setCustomerNo(no);
        SampleCommand.CustomerInfo.CustomerName name = requestFactory
            .createSampleCommandCustomerInfoCustomerName();
        customer.setCustomerName(name);
        SampleCommand.CustomerInfo.CustomerAddress address = requestFactory
            .createSampleCommandCustomerInfoCustomerAddress();
        customer.setCustomerAddress(address);
        SampleCommand.CustomerInfo.SearchDate searchDate = requestFactory
            .createSampleCommandCustomerInfoSearchDate();
        customer.setSearchDate(searchDate);

        no.setValue("1111");
        name.setValue("‚¢‚¢‚¢‚¢‚¢‚¢‚¢");
        address.setValue("‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ");
        searchDate.setValue("20060701");
        
        JaxbBindingProvider binder = new JaxbBindingProvider();

        
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
        SampleResponse response = (SampleResponse) binder.xmlToJavaObject(
            responseXML, SampleResponse.class);

        List<SampleResponse.CustomerList> customers = response
            .getCustomerList();

        for (int i = 0; i < customers.size(); i++) {
            String cust_no = customers.get(i).getCustomerNo().getValue();
            String cust_name = customers.get(i).getCustomerName().getValue();
            String cust_address = customers.get(i).getCustomerAddress().getValue();
            long cust_amount = customers.get(i).getCustomerAmount().getValue();
            long cust_charge = customers.get(i).getCustomerCharge().getValue();
            System.out.println("Customer number = " + cust_no);
            System.out.println("Customer name = " + cust_name);
            System.out.println("Customer address = " + cust_address);
            System.out.println("Customer amount = " + cust_amount);
            System.out.println("Customer charge = " + cust_charge);
            System.out.println();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
