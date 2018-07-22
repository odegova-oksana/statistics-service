package com.codingtask.statistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsRestTest {
	
	private MediaType contentType = 
			new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getStatisticsSuccessfully() throws Exception {
		
		addTransaction(12.3, (System.currentTimeMillis() - 500));
		addTransaction(7.7, (System.currentTimeMillis() - 500));
		addTransaction(10.0, (System.currentTimeMillis() - 500));
		addTransaction(5.0, System.currentTimeMillis());
		addTransaction(15.0, System.currentTimeMillis());
		addTransaction(11.0, System.currentTimeMillis());
		addTransaction(9.0, System.currentTimeMillis());
		
		mockMvc.perform(get("/api/statistics"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.sum", is(70.0)))
			.andExpect(jsonPath("$.avg", is(10.0)))
			.andExpect(jsonPath("$.max", is(15.0)))
			.andExpect(jsonPath("$.min", is(5.0)))
			.andExpect(jsonPath("$.count", is(7)));
	}
	
	private void addTransaction(double amount, long timestamp) throws Exception{
		String transaction = "{\"amount\":" + amount + ",\"timestamp\":" + timestamp + "}";
		mockMvc.perform(post("/api/transactions").content(transaction).contentType(contentType)).andExpect(status().is(201));
	}
	
	@Test
	public void addExpiredTransaction() throws Exception {
		String transaction = "{\"amount\":12.3,\"timestamp\":" + (System.currentTimeMillis() - 65000) + "}";		
		mockMvc.perform(post("/api/transactions").content(transaction).contentType(contentType)).andExpect(status().is(204));
	}

}
