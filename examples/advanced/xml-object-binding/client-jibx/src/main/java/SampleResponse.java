import java.util.ArrayList;

public class SampleResponse {

    private String numberOfRecords;
	private String searchDate;
    private ArrayList customerList = new ArrayList();

	public String getNumberOfRecords() {
			return this.numberOfRecords;
	}

	public void setNumberOfRecords(String numberOfRecords) {
			this.numberOfRecords = numberOfRecords;
	}

	public String getSearchDate() {
			return this.searchDate;
	}

	public void setSearchDate(String searchDate) {
			this.searchDate = searchDate;
	}


	public ArrayList getCustomerList() {
			return this.customerList;
	}

	public void setCustomerList(ArrayList customerList) {
			this.customerList = customerList;
	}
}
