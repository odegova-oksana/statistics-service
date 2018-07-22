package com.codingtask.statistics.model;

import java.util.ArrayList;

import java.util.List;

public class TransactionsPool {
	
	private Bucket[] pool = new Bucket[60];
	
	private List<Bucket> getBuckets() {
		List<Bucket> buckets = new ArrayList<>();
		for (int i = 0; i < pool.length; i++) {
			if (pool[i] != null && !bucketIsExpired(pool[i])) {
				buckets.add(pool[i]);
			}
		}
		return buckets;
	}

	private boolean bucketIsExpired(Bucket theBucket) {
		return System.currentTimeMillis() / 1000 - theBucket.getBucketSec() > 60000; 
	}
	
	public Bucket getBucket(int bucketNumber, long transactionSec) {
		if (pool[bucketNumber] == null) {
			pool[bucketNumber] = new Bucket(transactionSec);
		}
		return pool[bucketNumber];
	}

	public double getSum() {
		return getBuckets().isEmpty() ? 0.0 : getBuckets().stream().mapToDouble(item -> item.getSum()).sum();
	}
	
	public double getAvg() {
		return getBuckets().isEmpty() ? 0.0 : getSum() / getCount();
	}
	
	public double getMax() {
		return getBuckets().isEmpty() ? 0.0 : getBuckets().stream().mapToDouble(item -> item.getMax()).max().getAsDouble();
	}
	
	public double getMin() {
		return getBuckets().isEmpty() ? 0.0 : getBuckets().stream().mapToDouble(item -> item.getMin()).min().getAsDouble();
	}
	
	public long getCount() {
		return getBuckets().stream().mapToLong(item -> item.getCount()).sum();
	}

}
