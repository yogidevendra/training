## Synopsis

###Problem Statement
Generate the simulated input data set like transactions from Banking Application which can be used further by Apex.
Also make sure input data can be tuned with certain parameters.

###Description
Single Transaction may contain account number, trans id, amount and some helping members. Provide a way to tune the data set to
simulate according to the required through certain parameters. They may apply on any member in a transaction.
For eg. High value transactions (ratio provided to simulate the data set).

####Arguments
Property File: It contains pre-defined properties which may impact transactional data been generated.
OutputFile: Contains the transactional data.

####How to Run
```
DataGenerator <propertyFile.properties> <outputFile.txt>
```

####Output
```
<TransactionID>, <AccountNumber>, <TransactionAmount>
```
