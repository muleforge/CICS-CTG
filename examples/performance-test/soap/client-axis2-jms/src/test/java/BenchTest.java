
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
public class BenchTest extends TestCase {

	private static String repositoryPath =null;
	private static String axis2XmlPath =null;
	private static String connectionFactory =null;
	private static String initialFactory =null;
	private static String brokerURL =null;
	private static String queueName =null;

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public BenchTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(BenchTest.class);
	}

	public void test1() {
		try{
			repositoryPath = System.getProperty("AXIS2_REPOSITORY_PATH");
			axis2XmlPath = System.getProperty("AXIS2_XML_PATH");
			connectionFactory = System.getProperty("CONNECTION_FACTORY");
			initialFactory = System.getProperty("INITIAL_FACTORY");
			brokerURL = System.getProperty("BROKER_URL");
			queueName = System.getProperty("QUEUE_NAME");
			int noOfThreads = Integer.parseInt(System.getProperty("NO_OF_THREADS"));
			System.out.println("NO_OF_THREADS : "+noOfThreads);
			int noOfIterations = Integer.parseInt(System.getProperty("NO_OF_ITERATIONS"));
			System.out.println("NO_OF_ITERATIONS : "+noOfIterations);
			int noOfRecords = Integer.parseInt(System.getProperty("NO_OF_RECORDS"));
			System.out.println("NO_OF_RECORDS : "+noOfRecords);
			String verbose = System.getProperty("VERBOSE");
			System.out.println("VERBOSE : "+verbose);
			
			long total_start = System.currentTimeMillis();

			for (int i = 0; i < noOfThreads; i++) {
				TestPerformance test = new TestPerformance();
				test.setNoOfIterations(noOfIterations);
				test.setNoOfRecords(noOfRecords);
				//Set logging
				if(verbose.equalsIgnoreCase("true")){
					test.setVerbose(true);
				}
				Thread thread = new Thread(test);
				thread.start();
				thread.join();
			}

			long total_end = System.currentTimeMillis();
			System.out.println("-------------------------------------------------------------");
			long total_time = total_end - total_start;
			double throughput = (noOfIterations*noOfThreads)*1000.0/total_time;

			System.out.println("benchmark takes "+total_time/1000 + "." + total_time%1000+" seconds");
			System.out.println("throughput "+throughput);

			System.out.println("-------------------------------------------------------------");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public class TestPerformance implements Runnable {
		
		
		private boolean verbose;
		private int noOfRecords;
		private int noOfIterations=1;
		
		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}

		public int getNoOfIterations() {
			return noOfIterations;
		}

		public void setNoOfIterations(int noOfIterations) {
			this.noOfIterations = noOfIterations;
		}

		public int getNoOfRecords() {
			return noOfRecords;
		}

		public void setNoOfRecords(int noOfRecords) {
			this.noOfRecords = noOfRecords;
		}

		private MuleCicsUMOStub getMuleCicsStub() throws Exception {
			//This example loads the axis confi guration 
			//from filesystem.Another way to load is,
			//to modify the axis2.xml in axis*.jar and pass null parameters to ConfigurationContextFactory.createConfigurationContextFromFileSystem
			//for e.g: ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem( null , null );
			ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem( repositoryPath , axis2XmlPath );
			String endpointURL = "jms:/"+queueName+"?" + JMSConstants.CONFAC_JNDI_NAME_PARAM+"="+connectionFactory+"&java.naming.factory.initial="+initialFactory+"&java.naming.provider.url="+brokerURL;
			System.out.println("endpointURL->"+endpointURL);
			MuleCicsUMOStub stub = new MuleCicsUMOStub(context,endpointURL);
			return stub;
		}

		private void perfTestCommand10()  {
			if(verbose){
				System.out.println("In perfTestCommand10()");
			}
			MuleCicsUMOStub.PerfTestCommand10 perfTestCommand10 = new MuleCicsUMOStub.PerfTestCommand10();
			MuleCicsUMOStub.PerfTestCommandType perfCommandType = new MuleCicsUMOStub.PerfTestCommandType();
			perfTestCommand10.setPerfTestCommand10(perfCommandType);
	
			MuleCicsUMOStub.CustomerInfo_type0 cust = new MuleCicsUMOStub.CustomerInfo_type0();
			perfCommandType.setCustomerInfo(cust);
	
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
	
			MuleCicsUMOStub stub;
			try {
				stub = getMuleCicsStub();
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse10 response = stub.perfTestCommand10(perfTestCommand10);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse10Type perfResponseType = response.getPerfTestResponse10();
				
				MuleCicsUMOStub.CustomerListInf_type1[] customers = perfResponseType.getCustomerListInf();
				if(verbose){
					System.out.println("Search results for name=" + name );
				}
				for (int i=0; i<customers.length; i++) {
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"             ------------------------");
						System.out.println("FirstItemCode: '"      + customers[i].getFirstItemCode() +"'");
						System.out.println("FirstItemJapan: '"    + customers[i].getFirstItemJapan() +"'");
						System.out.println("SecondItemCode: '"      + customers[i].getSecondItemCode() +"'");
						System.out.println("SecondItemJapan: '"      + customers[i].getSecondItemJapan() +"'");
						System.out.println("ThirdItemCode: '"      + customers[i].getThirdItemCode() +"'");
						System.out.println("ThirdItemJapan: '"      + customers[i].getThirdItemJapan() +"'");
						System.out.println("FourthItemCode: '"      + customers[i].getFourthItemCode() +"'");
						System.out.println("FourthItemJapan: '"      + customers[i].getFourthItemJapan() +"'");
						System.out.println("FifthItemCode: '"      + customers[i].getFifthItemCode() +"'");
						System.out.println("FifthItemJapan: '"      + customers[i].getFifthItemJapan() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+customers.length+" records = " + (end-start) + " milliseconds");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand50() {
			if(verbose){
				System.out.println("In perfTestCommand50()");
			}
			MuleCicsUMOStub.PerfTestCommand50 perfTestCommand10 = new MuleCicsUMOStub.PerfTestCommand50();
			MuleCicsUMOStub.PerfTestCommandType perfCommandType = new MuleCicsUMOStub.PerfTestCommandType();
			perfTestCommand10.setPerfTestCommand50(perfCommandType);
	
			MuleCicsUMOStub.CustomerInfo_type0 cust = new MuleCicsUMOStub.CustomerInfo_type0();
			perfCommandType.setCustomerInfo(cust);
	
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
	
			try {
				MuleCicsUMOStub stub = getMuleCicsStub();
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse50 response = stub.perfTestCommand50(perfTestCommand10);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse50Type perfResponseType = response.getPerfTestResponse50();
				MuleCicsUMOStub.CustomerListInf_type3[] customers = perfResponseType.getCustomerListInf();
				System.out.println("Search results for name=" + name );
				for (int i=0; i<customers.length; i++) {
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"             ------------------------");
						System.out.println("FirstItemCode: '"      + customers[i].getFirstItemCode() +"'");
						System.out.println("FirstItemJapan: '"    + customers[i].getFirstItemJapan() +"'");
						System.out.println("SecondItemCode: '"      + customers[i].getSecondItemCode() +"'");
						System.out.println("SecondItemJapan: '"      + customers[i].getSecondItemJapan() +"'");
						System.out.println("ThirdItemCode: '"      + customers[i].getThirdItemCode() +"'");
						System.out.println("ThirdItemJapan: '"      + customers[i].getThirdItemJapan() +"'");
						System.out.println("FourthItemCode: '"      + customers[i].getFourthItemCode() +"'");
						System.out.println("FourthItemJapan: '"      + customers[i].getFourthItemJapan() +"'");
						System.out.println("FifthItemCode: '"      + customers[i].getFifthItemCode() +"'");
						System.out.println("FifthItemJapan: '"      + customers[i].getFifthItemJapan() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+customers.length+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand100() {
			System.out.println("In perfTestCommand100()");
			MuleCicsUMOStub.PerfTestCommand100 perfTestCommand10 = new MuleCicsUMOStub.PerfTestCommand100();
			MuleCicsUMOStub.PerfTestCommandType perfCommandType = new MuleCicsUMOStub.PerfTestCommandType();
			perfTestCommand10.setPerfTestCommand100(perfCommandType);
	
			MuleCicsUMOStub.CustomerInfo_type0 cust = new MuleCicsUMOStub.CustomerInfo_type0();
			perfCommandType.setCustomerInfo(cust);
	
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
			try{
				MuleCicsUMOStub stub = getMuleCicsStub();
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse100 response = stub.perfTestCommand100(perfTestCommand10);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse100Type perfResponseType = response.getPerfTestResponse100();

				MuleCicsUMOStub.CustomerListInf_type2[] customers = perfResponseType.getCustomerListInf();
				System.out.println("Search results for name=" + name );
				for (int i=0; i<customers.length; i++) {
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + customers[i].getFirstItemCode() +"'");
						System.out.println("FirstItemJapan: '"    + customers[i].getFirstItemJapan() +"'");
						System.out.println("SecondItemCode: '"      + customers[i].getSecondItemCode() +"'");
						System.out.println("SecondItemJapan: '"      + customers[i].getSecondItemJapan() +"'");
						System.out.println("ThirdItemCode: '"      + customers[i].getThirdItemCode() +"'");
						System.out.println("ThirdItemJapan: '"      + customers[i].getThirdItemJapan() +"'");
						System.out.println("FourthItemCode: '"      + customers[i].getFourthItemCode() +"'");
						System.out.println("FourthItemJapan: '"      + customers[i].getFourthItemJapan() +"'");
						System.out.println("FifthItemCode: '"      + customers[i].getFifthItemCode() +"'");
						System.out.println("FifthItemJapan: '"      + customers[i].getFifthItemJapan() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+customers.length+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand500() {
			if(verbose){
				System.out.println("In perfTestCommand500()");
			}
			MuleCicsUMOStub.PerfTestCommand500 perfTestCommand10 = new MuleCicsUMOStub.PerfTestCommand500();
			MuleCicsUMOStub.PerfTestCommandType perfCommandType = new MuleCicsUMOStub.PerfTestCommandType();
			perfTestCommand10.setPerfTestCommand500(perfCommandType);
	
			MuleCicsUMOStub.CustomerInfo_type0 cust = new MuleCicsUMOStub.CustomerInfo_type0();
			perfCommandType.setCustomerInfo(cust);
	
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
			try{
				MuleCicsUMOStub stub = getMuleCicsStub();
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse500 response = stub.perfTestCommand500(perfTestCommand10);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse500Type perfResponseType = response.getPerfTestResponse500();

				MuleCicsUMOStub.CustomerListInf_type0[] customers = perfResponseType.getCustomerListInf();
				System.out.println("Search results for name=" + name );
				for (int i=0; i<customers.length; i++) {
					if(verbose){
System.out.println("----------------- Iteration # "+(i+1)+"             ------------------------");
					System.out.println("FirstItemCode: '"      + customers[i].getFirstItemCode() +"'");
					System.out.println("FirstItemJapan: '"    + customers[i].getFirstItemJapan() +"'");
					System.out.println("SecondItemCode: '"      + customers[i].getSecondItemCode() +"'");
					System.out.println("SecondItemJapan: '"      + customers[i].getSecondItemJapan() +"'");
					System.out.println("ThirdItemCode: '"      + customers[i].getThirdItemCode() +"'");
					System.out.println("ThirdItemJapan: '"      + customers[i].getThirdItemJapan() +"'");
					System.out.println("FourthItemCode: '"      + customers[i].getFourthItemCode() +"'");
					System.out.println("FourthItemJapan: '"      + customers[i].getFourthItemJapan() +"'");
					System.out.println("FifthItemCode: '"      + customers[i].getFifthItemCode() +"'");
					System.out.println("FifthItemJapan: '"      + customers[i].getFifthItemJapan() +"'");
					System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+customers.length+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			System.out.println("In run method");
			for(int i=0;i<noOfIterations;i++){
				switch( noOfRecords ){
				case 10:
					perfTestCommand10();
					break;
				case 50:
					perfTestCommand50();
					break;
				case 100:
					perfTestCommand100();
					break;
				default:
					perfTestCommand500();
				break;
				}
			}
		}
	}
}
