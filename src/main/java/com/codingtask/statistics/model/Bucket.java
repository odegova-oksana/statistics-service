package com.codingtask.statistics.model;

import java.util.ArrayList;
import java.util.List;

public class Bucket {

	private long bucketSec;
	private List<Transaction> transactions = new ArrayList<>();

	public Bucket(long bucketSec) {
		this.bucketSec = bucketSec;
	}

	public long getBucketSec() {
		return bucketSec;
	}

	public void setBucketSec(long bucketSec) {
		this.bucketSec = bucketSec;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public double getSum() {
		return transactions.isEmpty() ? 0.0 : transactions.stream().mapToDouble(item -> item.getAmount()).sum();
	}
	
	public double getAvg() {
		return transactions.isEmpty() ? 0.0 : getSum() / transactions.size();
	}
	
	public double getMax() {
		return transactions.isEmpty() ? 0.0 : transactions.stream().mapToDouble(item -> item.getAmount()).max().getAsDouble();
	}
	
	public double getMin() {
		return transactions.isEmpty() ? 0.0 : transactions.stream().mapToDouble(item -> item.getAmount()).min().getAsDouble();
	}
	
	public long getCount() {
		return transactions.size();
	}

}
