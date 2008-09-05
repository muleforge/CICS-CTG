
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import customerinterface.*;
public class SampleCommandClientTest extends TestCase {
				
	/**
	   * Create the test case
	   *
	   * @param testName name of the test case
	   */
	  public SampleCommandClientTest(String testName) {
		super(testName);
	  }

	  /**
	   * @return the suite of tests being tested
	   */
	  public static Test suite()
	  {
		return new TestSuite(SampleCommandClientTest.class);
	  }

	  /**
	   * Rigourous Test :-)
	   */
	  public void testCicsWebservice() throws Exception{

		  
		  MuleCicsUMO mulecicsUMO = new MuleCicsUMO();
		  
		  MuleCicsProxy proxy=mulecicsUMO.getMuleCicsUMO();
		  SampleCommandType sampleCommand =new SampleCommandType();
		  
		  SampleCommandType.CustomerInfo cust=new SampleCommandType.CustomerInfo();
		  
		  SampleCommandType.CustomerInfo.CustomerNo no=new SampleCommandType.CustomerInfo.CustomerNo();
		  no.setValue("1111");
		  SampleCommandType.CustomerInfo.CustomerName name=new SampleCommandType.CustomerInfo.CustomerName();
		  name.setValue("‚¢‚¢‚¢‚¢‚¢‚¢‚¢"); // ‹´–{
		  SampleCommandType.CustomerInfo.CustomerAddress address=new SampleCommandType.CustomerInfo.CustomerAddress();
		  address.setValue("‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ");
		  SampleCommandType.CustomerInfo.SearchDate searchdate = new SampleCommandType.CustomerInfo.SearchDate();
		  searchdate.setValue("20060701");
		  
		  cust.setCustomerNo(no);
		  cust.setCustomerName(name);
		  cust.setCustomerAddress(address);
		  cust.setSearchDate(searchdate);
		  sampleCommand.setCustomerInfo(cust);
		  
		  javax.xml.ws.Holder<SampleResponseType.NoOfRecords> noOfRecords=new javax.xml.ws.Holder<SampleResponseType.NoOfRecords>(new SampleResponseType.NoOfRecords());
		  javax.xml.ws.Holder<SampleResponseType.SearchDate> searchDate=new javax.xml.ws.Holder<SampleResponseType.SearchDate>(new SampleResponseType.SearchDate());
		  javax.xml.ws.Holder<java.util.List<customerinterface.SampleResponseType.CustomerList>> customerList=new javax.xml.ws.Holder<java.util.List<customerinterface.SampleResponseType.CustomerList>>();
		  
		  proxy.sampleCommand(cust, noOfRecords, searchDate, customerList);
		  
		  java.util.List customers=customerList.value;
		  for(int i=0;i<customers.size();i++){
			  SampleResponseType.CustomerList customer=(SampleResponseType.CustomerList)customers.get(i);
			  System.out.println("No: '"      + customer.getCustomerNo().getValue());
			  System.out.println("Name: '"    + customer.getCustomerName().getValue());
			  System.out.println("Address:' " + customer.getCustomerAddress().getValue());
			  System.out.println("Amount: '"  + customer.getCustomerAmount().getValue());
			  System.out.println("Charge: '"  + customer.getCustomerCharge().getValue());
			  System.out.println("");
		  }
	  }
}
