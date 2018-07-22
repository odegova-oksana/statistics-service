package com.codingtask.statistics.service;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.codingtask.statistics.exceptions.ExpiredTransactionException;
import com.codingtask.statistics.model.Bucket;
import com.codingtask.statistics.model.Statistics;
import com.codingtask.statistics.model.Transaction;
import com.codingtask.statistics.model.TransactionsPool;

@Service
public class StatisticsServiceImpl implements StatisticsService {
	
	private TransactionsPool transactionsPool;
	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
	
	@PostConstruct
	private void init() {
		transactionsPool = new TransactionsPool();
	}

	@Override
	public Statistics getStatistics() {
		Statistics statistics = new Statistics();
		
		Lock lock = readWriteLock.readLock();
		try {
			lock.lock();
			
			statistics.setSum(transactionsPool.getSum());
			statistics.setAvg(transactionsPool.getAvg());
			statistics.setMax(transactionsPool.getMax());
			statistics.setMin(transactionsPool.getMin());
			statistics.setCount(transactionsPool.getCount());
			
		} finally {
			lock.unlock();
		}
		
		return statistics;
	}

	@Override
	public void addTransaction(Transaction transaction) throws ExpiredTransactionException{
		
		if (transaction.isExpired()) {
			throw new ExpiredTransactionException();
		}
		
		long transactionSec = transaction.getTimestamp() / 1000;
		int bucketNumber = getBucketNumber(transactionSec);
		
		Lock lock = readWriteLock.writeLock();
		try {
			lock.lock();
			
			Bucket bucket = transactionsPool.getBucket(bucketNumber, transactionSec);
			
			if (bucket.getBucketSec() < transactionSec) {
				bucket.setBucketSec(transactionSec);
				bucket.setTransactions(new ArrayList<>());
			}
			
			bucket.getTransactions().add(transaction);
			
		} finally {
			lock.unlock();
		}
	}
	
	private int getBucketNumber(long transactionSec) {
		return (int) (transactionSec % 60);
	}
	
}
