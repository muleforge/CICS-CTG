
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import customerinterface.*;
import customerinterface.PerfTestCommandType.CustomerInfo.*;

/**
 * Unit test for CICSRecord
 */
public class BenchTest extends TestCase {
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

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		try {
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
		private int noOfRecords;
		private int noOfIterations=1;
		private boolean verbose;
		
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
		
		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}

		public void run() {
			if(verbose){
				System.out.println("In run method");
			}
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
		
		private void perfTestCommand10(){
			if(verbose){
				System.out.println("In perfTestCommand10()");
			}
			try{
	            System.out.println("**************************************");
				MuleCicsUMO muleCicsUMO = new MuleCicsUMO();
				MuleCicsProxy proxy = muleCicsUMO.getMuleCicsUMO();
				PerfTestCommandType.CustomerInfo custInfo = new PerfTestCommandType.CustomerInfo();
				
				CustomerAddress address = new CustomerAddress();
				address.setValue("abc");
				custInfo.setCustomerAddress(address);
				
				CustomerName name = new CustomerName();
				name.setValue("橋本"); // 橋本
				custInfo.setCustomerName(name);
				
				CustomerNo no = new CustomerNo();
				no.setValue("111"); 
				custInfo.setCustomerName(name);
				
				SearchDate searchDate = new SearchDate();
				searchDate.setValue("20060701"); 
				custInfo.setCustomerName(name);
				long start = System.currentTimeMillis();
				java.util.List<customerinterface.PerfTestResponse10Type.CustomerListInf> custList = proxy.perfTestCommand10(custInfo);
				long end = System.currentTimeMillis();
				
				if(verbose){
					System.out.println("Search results for name=" + name );
				}
				for (int i = 0; i < custList.size(); i++) {
					PerfTestResponse10Type.CustomerListInf cust = custList.get(i);
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + cust.getFirstItemCode().getValue() +"'");
						System.out.println("FirstItemJapan: '"    + cust.getFirstItemJapan().getValue() +"'");
						System.out.println("SecondItemCode: '"      + cust.getSecondItemCode().getValue() +"'");
						System.out.println("SecondItemJapan: '"      + cust.getSecondItemJapan().getValue() +"'");
						System.out.println("ThirdItemCode: '"      + cust.getThirdItemCode().getValue() +"'");
						System.out.println("ThirdItemJapan: '"      + cust.getThirdItemJapan().getValue() +"'");
						System.out.println("FourthItemCode: '"      + cust.getFourthItemCode().getValue() +"'");
						System.out.println("FourthItemJapan: '"      + cust.getFourthItemJapan().getValue() +"'");
						System.out.println("FifthItemCode: '"      + cust.getFifthItemCode().getValue() +"'");
						System.out.println("FifthItemJapan: '"      + cust.getFifthItemJapan().getValue() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.size()+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand50(){
			if(verbose){
				System.out.println("In perfTestCommand50()");
			}
			try{
	            System.out.println("**************************************");
				MuleCicsUMO muleCicsUMO = new MuleCicsUMO();
				MuleCicsProxy proxy = muleCicsUMO.getMuleCicsUMO();
				PerfTestCommandType.CustomerInfo custInfo = new PerfTestCommandType.CustomerInfo();
				
				CustomerAddress address = new CustomerAddress();
				address.setValue("abc");
				custInfo.setCustomerAddress(address);
				
				CustomerName name = new CustomerName();
				name.setValue("橋本"); // 橋本
				custInfo.setCustomerName(name);
				
				CustomerNo no = new CustomerNo();
				no.setValue("111"); 
				custInfo.setCustomerName(name);
				
				SearchDate searchDate = new SearchDate();
				searchDate.setValue("20060701"); 
				custInfo.setCustomerName(name);
				long start = System.currentTimeMillis();
				java.util.List<customerinterface.PerfTestResponse50Type.CustomerListInf> custList = proxy.perfTestCommand50(custInfo);
				long end = System.currentTimeMillis();
				
				if(verbose){
					System.out.println("Search results for name=" + name );
				}
				for (int i = 0; i < custList.size(); i++) {
					PerfTestResponse50Type.CustomerListInf cust = custList.get(i);
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + cust.getFirstItemCode().getValue() +"'");
						System.out.println("FirstItemJapan: '"    + cust.getFirstItemJapan().getValue() +"'");
						System.out.println("SecondItemCode: '"      + cust.getSecondItemCode().getValue() +"'");
						System.out.println("SecondItemJapan: '"      + cust.getSecondItemJapan().getValue() +"'");
						System.out.println("ThirdItemCode: '"      + cust.getThirdItemCode().getValue() +"'");
						System.out.println("ThirdItemJapan: '"      + cust.getThirdItemJapan().getValue() +"'");
						System.out.println("FourthItemCode: '"      + cust.getFourthItemCode().getValue() +"'");
						System.out.println("FourthItemJapan: '"      + cust.getFourthItemJapan().getValue() +"'");
						System.out.println("FifthItemCode: '"      + cust.getFifthItemCode().getValue() +"'");
						System.out.println("FifthItemJapan: '"      + cust.getFifthItemJapan().getValue() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.size()+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand100(){
			if(verbose){
				System.out.println("In perfTestCommand100()");
			}
			try{
	            System.out.println("**************************************");
				MuleCicsUMO muleCicsUMO = new MuleCicsUMO();
				MuleCicsProxy proxy = muleCicsUMO.getMuleCicsUMO();
				PerfTestCommandType.CustomerInfo custInfo = new PerfTestCommandType.CustomerInfo();
				
				CustomerAddress address = new CustomerAddress();
				address.setValue("abc");
				custInfo.setCustomerAddress(address);
				
				CustomerName name = new CustomerName();
				name.setValue("橋本"); // 橋本
				custInfo.setCustomerName(name);
				
				CustomerNo no = new CustomerNo();
				no.setValue("111"); 
				custInfo.setCustomerName(name);
				
				SearchDate searchDate = new SearchDate();
				searchDate.setValue("20060701"); 
				custInfo.setCustomerName(name);
				long start = System.currentTimeMillis();
				java.util.List<customerinterface.PerfTestResponse100Type.CustomerListInf> custList = proxy.perfTestCommand100(custInfo);
				long end = System.currentTimeMillis();
				
				if(verbose){
					System.out.println("Search results for name=" + name );
				}
				for (int i = 0; i < custList.size(); i++) {
					PerfTestResponse100Type.CustomerListInf cust = custList.get(i);
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + cust.getFirstItemCode().getValue() +"'");
						System.out.println("FirstItemJapan: '"    + cust.getFirstItemJapan().getValue() +"'");
						System.out.println("SecondItemCode: '"      + cust.getSecondItemCode().getValue() +"'");
						System.out.println("SecondItemJapan: '"      + cust.getSecondItemJapan().getValue() +"'");
						System.out.println("ThirdItemCode: '"      + cust.getThirdItemCode().getValue() +"'");
						System.out.println("ThirdItemJapan: '"      + cust.getThirdItemJapan().getValue() +"'");
						System.out.println("FourthItemCode: '"      + cust.getFourthItemCode().getValue() +"'");
						System.out.println("FourthItemJapan: '"      + cust.getFourthItemJapan().getValue() +"'");
						System.out.println("FifthItemCode: '"      + cust.getFifthItemCode().getValue() +"'");
						System.out.println("FifthItemJapan: '"      + cust.getFifthItemJapan().getValue() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.size()+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand500(){
			if(verbose){
				System.out.println("In perfTestCommand500()");
			}
			try{
	            System.out.println("**************************************");
				MuleCicsUMO muleCicsUMO = new MuleCicsUMO();
				MuleCicsProxy proxy = muleCicsUMO.getMuleCicsUMO();
				PerfTestCommandType.CustomerInfo custInfo = new PerfTestCommandType.CustomerInfo();
				
				CustomerAddress address = new CustomerAddress();
				address.setValue("abc");
				custInfo.setCustomerAddress(address);
				
				CustomerName name = new CustomerName();
				name.setValue("橋本"); // 橋本
				custInfo.setCustomerName(name);
				
				CustomerNo no = new CustomerNo();
				no.setValue("111"); 
				custInfo.setCustomerName(name);
				
				SearchDate searchDate = new SearchDate();
				searchDate.setValue("20060701"); 
				custInfo.setCustomerName(name);
				long start = System.currentTimeMillis();
				java.util.List<customerinterface.PerfTestResponse500Type.CustomerListInf> custList = proxy.perfTestCommand500(custInfo);
				long end = System.currentTimeMillis();
				
				if(verbose){
					System.out.println("Search results for name=" + name );
				}
				for (int i = 0; i < custList.size(); i++) {
					PerfTestResponse500Type.CustomerListInf cust = custList.get(i);
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + cust.getFirstItemCode().getValue() +"'");
						System.out.println("FirstItemJapan: '"    + cust.getFirstItemJapan().getValue() +"'");
						System.out.println("SecondItemCode: '"      + cust.getSecondItemCode().getValue() +"'");
						System.out.println("SecondItemJapan: '"      + cust.getSecondItemJapan().getValue() +"'");
						System.out.println("ThirdItemCode: '"      + cust.getThirdItemCode().getValue() +"'");
						System.out.println("ThirdItemJapan: '"      + cust.getThirdItemJapan().getValue() +"'");
						System.out.println("FourthItemCode: '"      + cust.getFourthItemCode().getValue() +"'");
						System.out.println("FourthItemJapan: '"      + cust.getFourthItemJapan().getValue() +"'");
						System.out.println("FifthItemCode: '"      + cust.getFifthItemCode().getValue() +"'");
						System.out.println("FifthItemJapan: '"      + cust.getFifthItemJapan().getValue() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.size()+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
