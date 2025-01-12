package com.example.service;

import java.util.List;
import com.example.model.Transaction;

public interface TransactionService {
	
	 Transaction createTransaction(Transaction transaction);
	 Transaction updateTransaction(int id, Transaction transaction);
	 List<Transaction> getAllTransactions();
	 void deleteTransaction(int id);
	 Transaction getTransactionById(int id);
	 
}

