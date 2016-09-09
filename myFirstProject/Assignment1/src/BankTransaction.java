
public class BankTransaction {
	private	int transactionID;
	private int accountNumber;
	private	float transactionAmount;

	public BankTransaction(int transactionID, int accountNumber, float amount) {
		this.setTransactionID(transactionID);
		this.setTransactionAccount(accountNumber);
		this.setTransactionAmount(amount);
	}

	public int getTransactionID() {
		return transactionID;
	}

	public int getAccountNumber() {
		return accountNumber;
	}
	
	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionID(int newTransactionID) {
		transactionID = newTransactionID;
	}

	public void setTransactionAmount(float newTransactionAmount) {
		transactionAmount = newTransactionAmount;
	}

	public String outputToString() {
		
        int transactionID = new Integer(this.getTransactionID());
        int account = new Integer(this.getAccountNumber());
        float amount = new Float(this.getTransactionAmount());
		
        String transactionString = Integer.toString(transactionID) + ", " + Integer.toString(account) + ", " + Float.toString(amount);
        return transactionString;
       
	}

	public void setTransactionAccount(int accountNumber) {
		this.accountNumber = accountNumber;
		
	}

}
