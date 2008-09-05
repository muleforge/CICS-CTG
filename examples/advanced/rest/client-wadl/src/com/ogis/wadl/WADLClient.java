package com.ogis.wadl;

import java.util.Iterator;
import java.util.List;


public class WADLClient {

	public static void main(String[] args) throws Exception {
		String customerNo="1111";
		String customerName="‚¢‚¢‚¢‚¢‚¢‚¢‚¢";
		String customerAddress="‚ ‚ ‚ ‚ ‚ ‚ ‚ ‚ ";
		String searchDate="20060701";
		Endpoint.SampleCommand s = new Endpoint.SampleCommand();
		SampleResponse results = s.getAsSampleResponse(customerNo,customerName,customerAddress,searchDate);
		List resList=results.getCustomerList();
		Iterator itr = resList.iterator();
		while(itr.hasNext()){
			SampleResponse.CustomerList cust=(SampleResponse.CustomerList)itr.next();
			System.out.println("No :"+cust.getCustomerNo().getValue());
			System.out.println("Name:"+cust.getCustomerName().getValue());
			System.out.println("Address:"+cust.getCustomerAddress().getValue());
			System.out.println("Amount:"+cust.getCustomerAmount().getValue());
			System.out.println();
		}
	}
}
