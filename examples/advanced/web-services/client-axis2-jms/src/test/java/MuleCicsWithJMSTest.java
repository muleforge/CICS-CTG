
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis2.transport.jms.JMSConstants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import ogis.seriola.adb.*;

/**
 * Unit test for MuleCics webservice using JMS transport of Axis2.
 */
public class MuleCicsWithJMSTest extends TestCase {

	private static String repositoryPath =null;
	private static String axis2XmlPath =null;
	private static String connectionFactory =null;
	private static String initialFactory =null;
	private static String brokerURL =null;
	private static String queueName =null;

	 /**
	   * Create the test case
	   *
	   * @param testName name of the test case
	   */
	public MuleCicsWithJMSTest( String testName ) {
		super( testName );
		repositoryPath = System.getProperty("AXIS2_REPOSITORY_PATH");
		axis2XmlPath = System.getProperty("AXIS2_XML_PATH");
		connectionFactory = System.getProperty("CONN_FACTORY_JNDI_NAME");
		initialFactory = System.getProperty("INITIAL_CONTEXT_FACTORY");
		brokerURL = System.getProperty("BROKER_URL");
		queueName = System.getProperty("QUEUE_NAME");
		System.out.println("AXIS2_REPOSITORY_PATH: "+repositoryPath);
		System.out.println("AXIS2_XML_PATH: "+axis2XmlPath);
		System.out.println("INITIAL_CONTEXT_FACTORY: "+initialFactory);
		System.out.println("AXIS2_REPOSITORY_PATH: "+repositoryPath);
		System.out.println("BROKER_URL: "+brokerURL);
		System.out.println("QUEUE_NAME: "+queueName);
	}
	
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MuleCicsWithJMSTest.class);
	}

	
	private MuleCicsUMOStub getMuleCicsStub() throws Exception {
		//This example loads the axis configuration 
		//from filesystem.Another way to load is,
		//to modify the axis2.xml in axis*.jar and pass null parameters to ConfigurationContextFactory.createConfigurationContextFromFileSystem
		//for e.g: ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem( null , null );
		ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem( repositoryPath , axis2XmlPath );
		String endpointURL = "jms:/"+queueName+"?" + JMSConstants.CONFAC_JNDI_NAME_PARAM+"="+connectionFactory+"&java.naming.factory.initial="+initialFactory+"&java.naming.provider.url="+brokerURL;
		System.out.println("endpointURL->"+endpointURL);
		MuleCicsUMOStub stub = new MuleCicsUMOStub(context,endpointURL);
		return stub;
	}

	public void testSampleCommand() throws Exception{
		MuleCicsUMOStub.SampleCommand sampleCommand = new MuleCicsUMOStub.SampleCommand();
		MuleCicsUMOStub.SampleCommandType sampleCommandType = new MuleCicsUMOStub.SampleCommandType();
		sampleCommand.setSampleCommand(sampleCommandType);

		MuleCicsUMOStub.CustomerInfo_type0 cust = new MuleCicsUMOStub.CustomerInfo_type0();
		sampleCommandType.setCustomerInfo(cust);

		MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
		MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
		MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
		MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
		no.setString("111");
		name.setString("abc"); 
		address.setString("xyz");
		searchdate.setString("20060701");

		cust.setCustomerNo(no);
		cust.setCustomerName(name);
		cust.setCustomerAddress(address);
		cust.setSearchDate(searchdate);

		MuleCicsUMOStub stub = getMuleCicsStub();
		MuleCicsUMOStub.SampleResponse response = stub.sampleCommand(sampleCommand);
		MuleCicsUMOStub.SampleResponseType sampleResponseType = response.getSampleResponse();

		MuleCicsUMOStub.CustomerList_type0[] customers = sampleResponseType.getCustomerList();
		for (int i=0; i<customers.length; i++) {
			System.out.println("No: '"      + customers[i].getCustomerNo());
			System.out.println("Name: '"    + customers[i].getCustomerName());
			System.out.println("Address:' " + customers[i].getCustomerAddress());
			System.out.println("Amount: '"  + customers[i].getCustomerAmount());
			System.out.println("Charge: '"  + customers[i].getCustomerCharge());
			System.out.println("");
		}
	}

	public void testSampleListCommand() throws Exception {
		MuleCicsUMOStub.SampleListCommand sampleListCommand = new MuleCicsUMOStub.SampleListCommand();
		MuleCicsUMOStub.SampleCommandType sampleCommandType = new MuleCicsUMOStub.SampleCommandType();
		sampleListCommand.setSampleListCommand(sampleCommandType);

		MuleCicsUMOStub.CustomerInfo_type0 cust = new MuleCicsUMOStub.CustomerInfo_type0();
		sampleCommandType.setCustomerInfo(cust);

		MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
		MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
		MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
		MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
		no.setString("111");
		name.setString("abc"); 
		address.setString("xyz");
		searchdate.setString("20060701");

		cust.setCustomerNo(no);
		cust.setCustomerName(name);
		cust.setCustomerAddress(address);
		cust.setSearchDate(searchdate);

		MuleCicsUMOStub stub = getMuleCicsStub();
		MuleCicsUMOStub.SampleResponse response = stub.sampleListCommand(sampleListCommand);
		MuleCicsUMOStub.SampleResponseType sampleResponseType = response.getSampleResponse();

		MuleCicsUMOStub.CustomerList_type0[] customers = sampleResponseType.getCustomerList();
		for (int i=0; i<customers.length; i++) {
			System.out.println("No: '"      + customers[i].getCustomerNo());
			System.out.println("Name: '"    + customers[i].getCustomerName());
			System.out.println("Address:' " + customers[i].getCustomerAddress());
			System.out.println("Amount: '"  + customers[i].getCustomerAmount());
			System.out.println("Charge: '"  + customers[i].getCustomerCharge());
			System.out.println("");
		}

	}

	public void testSampleDuplicateCommand() throws Exception {
		MuleCicsUMOStub.SampleDuplicateCommand sampleDuplicateCommand = new MuleCicsUMOStub.SampleDuplicateCommand();
		MuleCicsUMOStub.SampleCommandType sampleCommandType = new MuleCicsUMOStub.SampleCommandType();
		sampleDuplicateCommand.setSampleDuplicateCommand(sampleCommandType);

		MuleCicsUMOStub.CustomerInfo_type0 cust = new MuleCicsUMOStub.CustomerInfo_type0();
		sampleCommandType.setCustomerInfo(cust);

		MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
		MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
		MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
		MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
		no.setString("111");
		name.setString("abc"); 
		address.setString("xyz");
		searchdate.setString("20060701");

		cust.setCustomerNo(no);
		cust.setCustomerName(name);
		cust.setCustomerAddress(address);
		cust.setSearchDate(searchdate);

		MuleCicsUMOStub stub = getMuleCicsStub();
		MuleCicsUMOStub.SampleResponse response = stub.sampleDuplicateCommand(sampleDuplicateCommand);
		MuleCicsUMOStub.SampleResponseType sampleResponseType = response.getSampleResponse();

		MuleCicsUMOStub.CustomerList_type0[] customers = sampleResponseType.getCustomerList();
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
