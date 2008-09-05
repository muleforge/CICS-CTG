import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import client.adb.*;

public class SampleDuplicateCommandClientTest extends TestCase  {
    
  /**
  * Create the test case
  *
  * @param testName name of the test case
  */
  public SampleDuplicateCommandClientTest(String testName) {
      super(testName);
  }

  /**
  * @return the suite of tests being tested
  */
  public static Test suite()
  {
      return new TestSuite(SampleDuplicateCommandClientTest.class);
  }

  /**
  * Rigourous Test :-)
  */
  public void testSeriolaWebservice() throws Exception
  {
      MuleCicsUMOStub stub = new MuleCicsUMOStub();
      MuleCicsUMOStub.SampleDuplicateCommand sampleDupCommand = new MuleCicsUMOStub.SampleDuplicateCommand();
      MuleCicsUMOStub.SampleCommandType sampleCommandType = new MuleCicsUMOStub.SampleCommandType();
      sampleDupCommand.setSampleDuplicateCommand(sampleCommandType);
      MuleCicsUMOStub.CustomerInfo_type0 custInfo =  new MuleCicsUMOStub.CustomerInfo_type0();
          
      MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
      no.setString("111");
      custInfo.setCustomerNo(no);
      
      MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
      name.setString("‹´–{"); // ‹´–{
      custInfo.setCustomerName(name);
          
      MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
      address.setString("abc");
      custInfo.setCustomerAddress(address);
      
      MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
      searchdate.setString("20060701");
      custInfo.setSearchDate(searchdate);
          
      sampleCommandType.setCustomerInfo(custInfo);
      MuleCicsUMOStub.SampleResponse response = stub.sampleDuplicateCommand(sampleDupCommand);
                
      MuleCicsUMOStub.SampleResponseType responseType = response.getSampleResponse();
      client.adb.MuleCicsUMOStub.CustomerList_type0[] customers =responseType.getCustomerList();
      for (int i=0; i<customers.length; i++) {
        System.out.println("No: '"      + customers[i].getCustomerNo());
        System.out.println("Name: '"    + customers[i].getCustomerName());
        System.out.println("Address:' " + customers[i].getCustomerAddress());
        System.out.println("Amount: '"  + customers[i].getCustomerAmount());
        System.out.println("Charge: '"  + customers[i].getCustomerCharge());
        System.out.println("");
      }
  }
}
