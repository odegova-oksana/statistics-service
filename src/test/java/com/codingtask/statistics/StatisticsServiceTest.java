package com.codingtask.statistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codingtask.statistics.exceptions.ExpiredTransactionException;
import com.codingtask.statistics.model.Statistics;
import com.codingtask.statistics.model.Transaction;
import com.codingtask.statistics.service.StatisticsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsServiceTest {
	
	@Autowired
	private StatisticsService statisticsService;
	
	@Test
	public void getStatisticsSuccessfully() throws Exception {
		
		addTransaction(12.3, (System.currentTimeMillis() - 500));
		addTransaction(7.7, (System.currentTimeMillis() - 500));
		addTransaction(10.0, (System.currentTimeMillis() - 500));
		addTransaction(5.0, System.currentTimeMillis());
		addTransaction(15.0, System.currentTimeMillis());
		addTransaction(11.0, System.currentTimeMillis());
		addTransaction(9.0, System.currentTimeMillis());
		
		Statistics statistics = statisticsService.getStatistics();
		
		assert(statistics.getSum() == 70.0);
		assert(statistics.getAvg() == 10.0);
		assert(statistics.getMax() == 15.0);
		assert(statistics.getMin() == 5.0);
		assert(statistics.getCount() == 7);
	}
	
	private void addTransaction(double amount, long timestamp) throws Exception{
		Transaction transaction = new Transaction(amount, timestamp);
		statisticsService.addTransaction(transaction);
	}
	
	@Test(expected = ExpiredTransactionException.class)
	public void addExpiredTransaction() throws Exception {
		Transaction transaction = new Transaction(9.0, System.currentTimeMillis() - 65000);
		statisticsService.addTransaction(transaction);
	}

}
