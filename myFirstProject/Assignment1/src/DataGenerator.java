import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class DataGenerator {

	private static Logger LOGGER = Logger.getLogger("Transactions.log");
	private static final int MAX_INT = 655556;
	private AtomicInteger currentTransactionID = new AtomicInteger(10000);
	private AtomicInteger currentTransactionNumber = new AtomicInteger(0);
	private BufferedWriter outputBufferedWriter;
	private DataSetProperty tunableDataSet = new DataSetProperty();
	
	
	public DataGenerator(String propertyFile, String resultFile) throws IOException {
		FileWriter outputFileWriter = new FileWriter(resultFile, true);
		outputFileWriter.write("");
		outputFileWriter.flush();
		this.outputBufferedWriter = new BufferedWriter(outputFileWriter);
		
		/*
		 * parse the transaction properties provided as input.
		 */ 
		DataSetProperty tuneDataSet = this.getTunableDataSet();
		this.parseTransactionProperties(tuneDataSet, propertyFile);
	}

	public DataSetProperty getTunableDataSet() {
		return this.tunableDataSet;
	}

	public BankTransaction generateTransaction(DataSetProperty tuneDataSet) {	
		
		/*
		 * Now get atomically maintained transaction ID along with account number and transaction amount
		 * Note: Min and Max range is automatically adjusted according to property specified for high or
		 * low value transaction(s). 
		 */
		int transactionID = this.getUniqueTransactionID();		
		int accountLength = tuneDataSet.getAccountLength();
		int accountNumber = this.generateAccountNumber(accountLength);	
		float amount = this.generateTransAmount(tuneDataSet);
		
		/*
		 * create new transaction initialized with generated values.
		 */
		BankTransaction newTransaction = new BankTransaction(transactionID, accountNumber, amount);
		
		return newTransaction;
	}
	
	public float generateTransAmount(DataSetProperty tuneDataSet) {
		
	    int minAmountRange = 1;
	    int maxAmountRange = MAX_INT;
		float thresholdAmount;
		float transactionAmount;
		Random random = new Random();
	    
		thresholdAmount = tuneDataSet.getLowThresholdAmount();
		
		/*
	     * As top value is exclusive hence 1 is added to it.
	     */
		transactionAmount = random.nextFloat() * (maxAmountRange - minAmountRange + 1) + minAmountRange;
		
		/*
		 * So if randomly generated amount is more  then threshold and has
		 * exceeded the limit ofahigh value transactions then we need to re-adjust 
		 * the amount.
		 */
		if ((transactionAmount > thresholdAmount) &&
				(currentTransactionNumber.get() > tuneDataSet.getHighValueLimit())) {

			maxAmountRange = (int) thresholdAmount; 
			transactionAmount = random.nextFloat() * (maxAmountRange - minAmountRange + 1) + minAmountRange;
		}
		
	    return transactionAmount;
	}
	
	public int generateAccountNumber(int accountLength) {
		Random randomNumber = new Random();
		String minAccountRange = Integer.toBinaryString((1 << (accountLength - 1)));
		String maxAccountRange = Integer.toBinaryString((1 << accountLength));
		int maxRange = Integer.parseInt(maxAccountRange) - Integer.parseInt(minAccountRange);
		int accountNumber = Integer.parseInt(minAccountRange) + randomNumber.nextInt(maxRange);
		return accountNumber;
	}

	public int getUniqueTransactionID() {
		this.currentTransactionNumber.getAndIncrement();
	 	return currentTransactionID.getAndIncrement();
	}
	
	public void writeToFile(BankTransaction transaction) {
		 try {
				BufferedWriter bufferedWriter = this.getbufferedWriter();
			 	String writeableString = transaction.outputToString();

	            System.out.println(writeableString);        
	            bufferedWriter.write(writeableString);
	            bufferedWriter.newLine();
	  	 } catch (IOException e) {
	  		  LOGGER.severe("Unable to write transaction to a file.");
	  		  e.printStackTrace();
	     }
	}
	
	private BufferedWriter getbufferedWriter() {
		return this.outputBufferedWriter;
	}

	public void parseTransactionProperties(DataSetProperty tuneDataSet, String propertyFile) {
		
		try {
		
			/*
			 * Load the properties from file.
			 */
			FileInputStream fileInput = new FileInputStream(propertyFile);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			/*
			 * Run through the keys and take appropriate action on transactions.
			 */
			Enumeration<Object> numKeys = properties.keys();
			while (numKeys.hasMoreElements()) {
				String key = (String) numKeys.nextElement();
				String value = properties.getProperty(key);
				tuneDataSet.setTransactionProperty(key, value);
				System.out.println(key + ": " + value);
			}
			
		} catch (FileNotFoundException e) {
				LOGGER.severe("Property file not found. Provide correct path to property file.");
				e.printStackTrace();
		} catch (IOException e) {
				LOGGER.severe("Unable to load properties from given property file");
				e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		
		try {
			DataGenerator dataGenerator = new DataGenerator(args[0], args[1]);
			
			/*
			 * now we can generate the banking transactional data.
			 */
			dataGenerator.generateData();
			
			/*
			 * perform some some cleanup.
			 */
			dataGenerator.teardown();
			
		} catch (IOException e) {
			LOGGER.severe("Unable to generate the data due to IO failures.");
			e.printStackTrace();
		}
		
		
	}

	private void teardown() {
		try {
			this.outputBufferedWriter.close();
		} catch (IOException e) {
			LOGGER.warning("Unable to close the resultant file.");
			e.printStackTrace();
		}		
	}

	private void generateData() {
		
		DataSetProperty tuneDataSet = this.getTunableDataSet();
		
		/*
		 * Now generate the transactions as per specified tunables.
		 */	
		int counter = tuneDataSet.getTotalNumOfTrans();
		while (counter > 0) {
			BankTransaction Trans = new BankTransaction(0, 0, (float) 0);
			Trans = generateTransaction(tuneDataSet);
			writeToFile(Trans);
			counter--;
		}
		
	}	
}
