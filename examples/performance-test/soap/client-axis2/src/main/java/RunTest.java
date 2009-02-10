
import ogis.seriola.adb.MuleCicsUMOStub;

/**
 * Unit test for CICSRecord
 */
public class RunTest {
	/**
     * Rigourous Test :-)
     */
	public void main(String[] args) throws Exception {
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
				MuleCicsUMOStub stub = new MuleCicsUMOStub();

                long soTimeout = 2 * 60 * 1000; // Two minutes
                stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(soTimeout);
                
				MuleCicsUMOStub.PerfTestCommand10 perfTest10 = new MuleCicsUMOStub.PerfTestCommand10();
				MuleCicsUMOStub.PerfTestCommandType perfCommandTestType = new MuleCicsUMOStub.PerfTestCommandType();
				perfTest10.setPerfTestCommand10(perfCommandTestType);
				MuleCicsUMOStub.CustomerInfo_type0 custInfo =  new MuleCicsUMOStub.CustomerInfo_type0();
	
				MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
				no.setString("111");
				custInfo.setCustomerNo(no);
	
				MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
				name.setString("橋本"); // 橋本
				custInfo.setCustomerName(name);
	
				MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
				address.setString("abc");
				custInfo.setCustomerAddress(address);
	
				MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
				searchdate.setString("20060701");
				custInfo.setSearchDate(searchdate);
	
				perfCommandTestType.setCustomerInfo(custInfo);

                
                
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse10 perfTestResponse = stub.perfTestCommand10(perfTest10);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse10Type responseType = perfTestResponse.getPerfTestResponse10();
				MuleCicsUMOStub.CustomerListInf_type1[] custList = responseType.getCustomerListInf();
				if(verbose){
					System.out.println("Search results for name=" + name );
				}
				for (int i = 0; i < custList.length; i++) {
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + custList[i].getFirstItemCode() +"'");
						System.out.println("FirstItemJapan: '"    + custList[i].getFirstItemJapan() +"'");
						System.out.println("SecondItemCode: '"      + custList[i].getSecondItemCode() +"'");
						System.out.println("SecondItemJapan: '"      + custList[i].getSecondItemJapan() +"'");
						System.out.println("ThirdItemCode: '"      + custList[i].getThirdItemCode() +"'");
						System.out.println("ThirdItemJapan: '"      + custList[i].getThirdItemJapan() +"'");
						System.out.println("FourthItemCode: '"      + custList[i].getFourthItemCode() +"'");
						System.out.println("FourthItemJapan: '"      + custList[i].getFourthItemJapan() +"'");
						System.out.println("FifthItemCode: '"      + custList[i].getFifthItemCode() +"'");
						System.out.println("FifthItemJapan: '"      + custList[i].getFifthItemJapan() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.length+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand50(){
			if(verbose){
				System.out.println("In perfTestCommand50()");
			}
			try{
				MuleCicsUMOStub stub = new MuleCicsUMOStub();
				MuleCicsUMOStub.PerfTestCommand50 perfTest50 = new MuleCicsUMOStub.PerfTestCommand50();
				MuleCicsUMOStub.PerfTestCommandType perfCommandTestType = new MuleCicsUMOStub.PerfTestCommandType();
				perfTest50.setPerfTestCommand50(perfCommandTestType);
				MuleCicsUMOStub.CustomerInfo_type0 custInfo =  new MuleCicsUMOStub.CustomerInfo_type0();
	
				MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
				no.setString("111");
				custInfo.setCustomerNo(no);
	
				MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
				name.setString("橋本"); // 橋本
				custInfo.setCustomerName(name);
	
				MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
				address.setString("abc");
				custInfo.setCustomerAddress(address);
	
				MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
				searchdate.setString("20060701");
				custInfo.setSearchDate(searchdate);
	
				perfCommandTestType.setCustomerInfo(custInfo);
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse50 perfTestResponse = stub.perfTestCommand50(perfTest50);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse50Type responseType = perfTestResponse.getPerfTestResponse50();
				MuleCicsUMOStub.CustomerListInf_type3[] custList = responseType.getCustomerListInf();
				for (int i = 0; i < custList.length; i++) {
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + custList[i].getFirstItemCode() +"'");
						System.out.println("FirstItemJapan: '"    + custList[i].getFirstItemJapan() +"'");
						System.out.println("SecondItemCode: '"      + custList[i].getSecondItemCode() +"'");
						System.out.println("SecondItemJapan: '"      + custList[i].getSecondItemJapan() +"'");
						System.out.println("ThirdItemCode: '"      + custList[i].getThirdItemCode() +"'");
						System.out.println("ThirdItemJapan: '"      + custList[i].getThirdItemJapan() +"'");
						System.out.println("FourthItemCode: '"      + custList[i].getFourthItemCode() +"'");
						System.out.println("FourthItemJapan: '"      + custList[i].getFourthItemJapan() +"'");
						System.out.println("FifthItemCode: '"      + custList[i].getFifthItemCode() +"'");
						System.out.println("FifthItemJapan: '"      + custList[i].getFifthItemJapan() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.length+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand100(){
			if(verbose){
				System.out.println("In perfTestCommand100()");
			}
			try{
				MuleCicsUMOStub stub = new MuleCicsUMOStub();
				MuleCicsUMOStub.PerfTestCommand100 perfTest100 = new MuleCicsUMOStub.PerfTestCommand100();
				MuleCicsUMOStub.PerfTestCommandType perfCommandTestType = new MuleCicsUMOStub.PerfTestCommandType();
				perfTest100.setPerfTestCommand100(perfCommandTestType);
				MuleCicsUMOStub.CustomerInfo_type0 custInfo =  new MuleCicsUMOStub.CustomerInfo_type0();
	
				MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
				no.setString("111");
				custInfo.setCustomerNo(no);
	
				MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
				name.setString("橋本"); // 橋本
				custInfo.setCustomerName(name);
	
				MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
				address.setString("abc");
				custInfo.setCustomerAddress(address);
	
				MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
				searchdate.setString("20060701");
				custInfo.setSearchDate(searchdate);
	
				perfCommandTestType.setCustomerInfo(custInfo);
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse100 perfTestResponse = stub.perfTestCommand100(perfTest100);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse100Type responseType = perfTestResponse.getPerfTestResponse100();
				MuleCicsUMOStub.CustomerListInf_type2[] custList = responseType.getCustomerListInf();
				for (int i = 0; i < custList.length; i++) {
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + custList[i].getFirstItemCode() +"'");
						System.out.println("FirstItemJapan: '"    + custList[i].getFirstItemJapan() +"'");
						System.out.println("SecondItemCode: '"      + custList[i].getSecondItemCode() +"'");
						System.out.println("SecondItemJapan: '"      + custList[i].getSecondItemJapan() +"'");
						System.out.println("ThirdItemCode: '"      + custList[i].getThirdItemCode() +"'");
						System.out.println("ThirdItemJapan: '"      + custList[i].getThirdItemJapan() +"'");
						System.out.println("FourthItemCode: '"      + custList[i].getFourthItemCode() +"'");
						System.out.println("FourthItemJapan: '"      + custList[i].getFourthItemJapan() +"'");
						System.out.println("FifthItemCode: '"      + custList[i].getFifthItemCode() +"'");
						System.out.println("FifthItemJapan: '"      + custList[i].getFifthItemJapan() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.length+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void perfTestCommand500(){
			if(verbose){
				System.out.println("In perfTestCommand500()");
			}
			try{
				MuleCicsUMOStub stub = new MuleCicsUMOStub();
				MuleCicsUMOStub.PerfTestCommand500 perfTest500 = new MuleCicsUMOStub.PerfTestCommand500();
				MuleCicsUMOStub.PerfTestCommandType perfCommandTestType = new MuleCicsUMOStub.PerfTestCommandType();
				perfTest500.setPerfTestCommand500(perfCommandTestType);
				MuleCicsUMOStub.CustomerInfo_type0 custInfo =  new MuleCicsUMOStub.CustomerInfo_type0();
	
				MuleCicsUMOStub.CustomerNo_type0 no = new MuleCicsUMOStub.CustomerNo_type0();
				no.setString("111");
				custInfo.setCustomerNo(no);
	
				MuleCicsUMOStub.CustomerName_type0 name = new MuleCicsUMOStub.CustomerName_type0();
				name.setString("橋本"); // 橋本
				custInfo.setCustomerName(name);
	
				MuleCicsUMOStub.CustomerAddress_type0 address = new MuleCicsUMOStub.CustomerAddress_type0();
				address.setString("abc");
				custInfo.setCustomerAddress(address);
	
				MuleCicsUMOStub.SearchDate_type0 searchdate = new MuleCicsUMOStub.SearchDate_type0();
				searchdate.setString("20060701");
				custInfo.setSearchDate(searchdate);
	
				perfCommandTestType.setCustomerInfo(custInfo);
				long start = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse500 perfTestResponse = stub.perfTestCommand500(perfTest500);
				long end = System.currentTimeMillis();
				MuleCicsUMOStub.PerfTestResponse500Type responseType = perfTestResponse.getPerfTestResponse500();
				MuleCicsUMOStub.CustomerListInf_type0[] custList = responseType.getCustomerListInf();
				for (int i = 0; i < custList.length; i++) {
					if(verbose){
						System.out.println("----------------- Iteration # "+(i+1)+"------------------------");
						System.out.println("FirstItemCode: '"      + custList[i].getFirstItemCode() +"'");
						System.out.println("FirstItemJapan: '"    + custList[i].getFirstItemJapan() +"'");
						System.out.println("SecondItemCode: '"      + custList[i].getSecondItemCode() +"'");
						System.out.println("SecondItemJapan: '"      + custList[i].getSecondItemJapan() +"'");
						System.out.println("ThirdItemCode: '"      + custList[i].getThirdItemCode() +"'");
						System.out.println("ThirdItemJapan: '"      + custList[i].getThirdItemJapan() +"'");
						System.out.println("FourthItemCode: '"      + custList[i].getFourthItemCode() +"'");
						System.out.println("FourthItemJapan: '"      + custList[i].getFourthItemJapan() +"'");
						System.out.println("FifthItemCode: '"      + custList[i].getFifthItemCode() +"'");
						System.out.println("FifthItemJapan: '"      + custList[i].getFifthItemJapan() +"'");
						System.out.println("---------------------------------------------------------------");
					}
				}
				System.out.println("Total time taken for "+custList.length+" records = " + (end-start) + " milliseconds");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
