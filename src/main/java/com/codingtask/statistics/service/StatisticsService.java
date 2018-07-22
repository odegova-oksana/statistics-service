package com.codingtask.statistics.service;

import com.codingtask.statistics.exceptions.ExpiredTransactionException;
import com.codingtask.statistics.model.Statistics;
import com.codingtask.statistics.model.Transaction;

public interface StatisticsService {

	public Statistics getStatistics();
	
	public void addTransaction(Transaction transaction) throws ExpiredTransactionException;
	
}
