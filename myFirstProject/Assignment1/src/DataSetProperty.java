
public class DataSetProperty {
	private float lowThresholdAmt;
	private int totalNumOfTransactions;
	private int numOfhighValueTransactions;
	private int numOfLowValueTransactions;
	private int accountLength;
	
	public void setLowThresholdAmount(float lowThreshold) {
		lowThresholdAmt = lowThreshold;
	}
	
	public void setTotalNumOfTransactions(int totalTransactions) {
		totalNumOfTransactions = totalTransactions;
	}
	
	public void setNumOfTransactionLimit(int highValuePercentage) {
		numOfhighValueTransactions = (highValuePercentage * totalNumOfTransactions)/100;
		numOfLowValueTransactions = totalNumOfTransactions - numOfhighValueTransactions;
	}
	
	public float getLowThresholdAmount() {
		return lowThresholdAmt;
	}
	
	public void setTransactionProperty(String key, String value) {
		switch (key) {
			case "LowThreshold":
				float setLow = Float.parseFloat(value);
				this.setLowThresholdAmount(setLow);
				break;
			case "highValuePercentage":
				int highValue = Integer.parseInt(value);
				this.setNumOfTransactionLimit(highValue);
				break;
			case "TotalNumofTrans":
				int setTotal = Integer.parseInt(value);
				this.setTotalNumOfTransactions(setTotal);
				break;
			case "NumOfDigitsForAccount":
				int accountLength = Integer.parseInt(value);
				this.setAccountLength(accountLength);
				break;
			default:
				throw new IllegalArgumentException("Invalid key element: " + key);
		}
	}

	private void setAccountLength(int accountLength) {
		this.accountLength = accountLength;
	}

	public int getTotalNumOfTrans() {
		return totalNumOfTransactions;
	}

	public int getHighValueLimit() {
		return numOfhighValueTransactions;
	}

	public int getAccountLength() {
		return accountLength;
	}
}
