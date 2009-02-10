package com.ogis.wadl;

import java.util.Iterator;
import java.util.List;
import com.ogis.wadl.*;

public class BenchTest implements Runnable {
	
	public static void main(String[] args) throws Exception {
		if( args.length < 4 ){
			System.out.println("Usage:");
			System.out.println("java BenchTest <No of records> <No of threads> <No of iterations>");
		}
		int cmdType = Integer.parseInt(args[0]);
		int noOfThreads = Integer.parseInt(args[1]);
		int noOfIterations = Integer.parseInt(args[2]);
		String verbose = args[3];
		
		System.out.println("VERBOSE : "+verbose);
		System.out.println("NO_OF_THREADS : "+noOfThreads);
		System.out.println("NO_OF_ITERATIONS : "+noOfIterations);
		System.out.println("NO_OF_RECORDS : "+cmdType);
		
		long total_start = System.currentTimeMillis();
		for (int i = 0; i < noOfThreads; i++) {
			BenchTest client = new BenchTest();
			client.setNoOfIterations(noOfIterations);
			client.setCmdType(cmdType);
			client.setVerbose(verbose.equals("true"));
			Thread thread = new Thread(client);
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
	}
	
	private int noOfIterations;
	private int cmdType;
	private boolean verbose;
	
	public void run() {
		for(int i=0;i<noOfIterations;i++){
			switch (cmdType) {
			case 10:
				perfTestCommand10();
				break;
			case 50:
				perfTestCommand50();
				break;
			case 100:
				perfTestCommand100();
				break;
			case 500:
				perfTestCommand500();
				break;
			default:
				perfTestCommand10();
				break;
			}
		}
	}
	
	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public int getCmdType() {
		return cmdType;
	}

	public void setCmdType(int cmdType) {
		this.cmdType = cmdType;
	}

	public int getNoOfIterations() {
		return noOfIterations;
	}

	public void setNoOfIterations(int noOfIterations) {
		this.noOfIterations = noOfIterations;
	}

	private void perfTestCommand10() {
		String customerNo="1111";
		String customerName="‚¢‚¢‚¢‚¢‚¢‚¢‚¢";
		String customerAddress="‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ";
		String searchDate="20060701";
		try{
			Endpoint.PerfTestCommand10 s = new Endpoint.PerfTestCommand10();
			long start = System.currentTimeMillis();
			PerfTestResponse10 results = s.getAsPerfTestResponse10(customerNo,customerName,customerAddress,searchDate);
			long end = System.currentTimeMillis();
			List resList=results.getCustomerListInf();
			Iterator itr = resList.iterator();
			if(verbose)
			System.out.println("Search results for name=" + customerName );
			while(itr.hasNext()){
				PerfTestResponse10.CustomerListInf cust=(PerfTestResponse10.CustomerListInf)itr.next();
				if(verbose){
					System.out.println("---------------------------------------------------");
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
			System.out.println("Total time taken for "+resList.size()+" records = " + (end-start) + " milliseconds");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void perfTestCommand50() {
		String customerNo="1111";
		String customerName="‚¢‚¢‚¢‚¢‚¢‚¢‚¢";
		String customerAddress="‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ";
		String searchDate="20060701";
		try{
			Endpoint.PerfTestCommand50 s = new Endpoint.PerfTestCommand50();
			long start = System.currentTimeMillis();
			PerfTestResponse50 results = s.getAsPerfTestResponse50(customerNo,customerName,customerAddress,searchDate);
			long end = System.currentTimeMillis();
			List resList=results.getCustomerListInf();
			Iterator itr = resList.iterator();
			if(verbose)
			System.out.println("Search results for name=" + customerName );
			while(itr.hasNext()){
				PerfTestResponse50.CustomerListInf cust=(PerfTestResponse50.CustomerListInf)itr.next();
				if(verbose){
					System.out.println("---------------------------------------------------");
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
			System.out.println("Total time taken for "+resList.size()+" records = " + (end-start) + " milliseconds");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void perfTestCommand100() {
		String customerNo="1111";
		String customerName="‚¢‚¢‚¢‚¢‚¢‚¢‚¢";
		String customerAddress="‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ";
		String searchDate="20060701";
		try{
			Endpoint.PerfTestCommand100 s = new Endpoint.PerfTestCommand100();
			long start = System.currentTimeMillis();
			PerfTestResponse100 results = s.getAsPerfTestResponse100(customerNo,customerName,customerAddress,searchDate);
			long end = System.currentTimeMillis();
			List resList=results.getCustomerListInf();
			Iterator itr = resList.iterator();
			if(verbose)
				System.out.println("Search results for name=" + customerName );
			while(itr.hasNext()){
				PerfTestResponse100.CustomerListInf cust=(PerfTestResponse100.CustomerListInf)itr.next();
				if(verbose){
					System.out.println("---------------------------------------------------");
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
			System.out.println("Total time taken for "+resList.size()+" records = " + (end-start) + " milliseconds");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void perfTestCommand500() {
		String customerNo="1111";
		String customerName="‚¢‚¢‚¢‚¢‚¢‚¢‚¢";
		String customerAddress="‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ";
		String searchDate="20060701";
		try{
			Endpoint.PerfTestCommand500 s = new Endpoint.PerfTestCommand500();
			long start = System.currentTimeMillis();
			PerfTestResponse500 results = s.getAsPerfTestResponse500(customerNo,customerName,customerAddress,searchDate);
			long end = System.currentTimeMillis();
			List resList=results.getCustomerListInf();
			Iterator itr = resList.iterator();
			if(verbose){
				System.out.println("Search results for name=" + customerName );
			}
			while(itr.hasNext()){
				PerfTestResponse500.CustomerListInf cust=(PerfTestResponse500.CustomerListInf)itr.next();
				if(verbose){
					System.out.println("---------------------------------------------------");
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
			System.out.println("Total time taken for "+resList.size()+" records = " + (end-start) + " milliseconds");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
