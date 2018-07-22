package com.codingtask.statistics.rest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingtask.statistics.exceptions.ExpiredTransactionException;
import com.codingtask.statistics.model.Statistics;
import com.codingtask.statistics.model.Transaction;
import com.codingtask.statistics.service.StatisticsService;

@RestController
@RequestMapping(path = "/api")
public class StatisticsRestController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatisticsService statisticsService;
	
	
	@GetMapping("/statistics")
	public Statistics getStatistics() {		
		logger.info("getStatistics for " + new Date());		
		Statistics statistics = statisticsService.getStatistics();		
		logger.info("statistics: " + statistics);	
		return statistics;
	}
	
	@PostMapping("/transactions")
	public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) {
		//201 - in case of success
		//204 - if transaction is older than 60 sec
		try {
			logger.info("addTransaction: timestamp: " + transaction.getTimestamp() + ", amount: " + transaction.getAmount());
			statisticsService.addTransaction(transaction);
			return ResponseEntity.status(201).body(null);
		} catch (ExpiredTransactionException e) {
			return ResponseEntity.status(204).body(null); 
		}
	}
	
}
