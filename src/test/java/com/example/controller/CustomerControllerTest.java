package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.model.Customer;
import com.example.repo.CustomerRepository;
import com.example.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CustomerService customerService;
	
	@MockBean
	private CustomerRepository customerRepository;
	
	@InjectMocks
	private CustomerController customerController;
	
	private Customer c1;
	private Customer c2;
	
	@BeforeEach
	void setUp() {
		c1=new Customer();
		c1.setId(1);
		c1.setName("ashwini");
		c1.setUsername("ash");
		c1.setPassword("As@123");
		c1.setBalance(5000);
	
		c2=new Customer();
		c2.setId(2);
		c2.setName("chandra");
		c2.setUsername("ash");
		c2.setPassword("As@123");
		c2.setBalance(5000);
	}
	
	@Test
	void testRead() throws Exception {
		List<Customer> custs=Arrays.asList(c1,c2);
		when(customerService.read()).thenReturn(custs);
		
		mockMvc.perform(get("/cust/read"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].name").value("ashwini"))
		.andExpect(jsonPath("$[1].name").value("chandra"));
	}
	
	
	@Test
	void testReadOne() throws Exception{
		  when(customerService.readOne(1)).thenReturn(ResponseEntity.ok(c1));
		
		mockMvc.perform(get("/cust/readOne/1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("ashwini"));
		
	}
	@Test
	void testAdd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String teamJson = objectMapper.writeValueAsString(c1);
        mockMvc.perform(post("/cust/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJson))
                .andExpect(status().isOk());
        verify(customerService).post(c1);	
	}
	@Test
	void testUpdate() throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		//String teamJson = objectMapper.writeValueAsString(c1);
		String customerJson = objectMapper.writeValueAsString(c1);
		 mockMvc.perform(put("/cust/update/1")
		.contentType(MediaType.APPLICATION_JSON)
		.content(customerJson))
		.andExpect(status().isOk());
		
		verify(customerService).update(c1,1);
	}
	
	@Test
	void testDelete() throws Exception{
		mockMvc.perform(delete("/cust/delete/1"))
		.andExpect(status().isOk());
		
		verify(customerService).delete(1);
	}
//	@Test
//	void testDeposit() throws Exception{
//		ObjectMapper objectMapper = new ObjectMapper();
//		//String teamJson = objectMapper.writeValueAsString(c1);
//		String customerJson = objectMapper.writeValueAsString(c1);
////		 mockMvc.perform(put("/cust/deposit/1/500")
////		.contentType(MediaType.APPLICATION_JSON)
////		.content(customerJson))
////		.andExpect(status().isOk())
////		.andExpect(jsonPath("$.balance").value(5500));
//		 
//		 String responseContent = mockMvc.perform(put("/cust/deposit/1/500")
//			        .contentType(MediaType.APPLICATION_JSON)
//			        .content(customerJson))
//			        .andExpect(status().isOk())
//			        .andReturn()
//			        .getResponse()
//			        .getContentAsString();
//
//			System.out.println("Response content: " + responseContent);
//
//		
//		verify(customerService).deposit(1,500);
//	}
//	
	@Test
	void testDeposit() throws Exception {
	    // Create a Customer object with the desired initial balance
	    Customer initialCustomer = new Customer();
	    initialCustomer.setId(1);
	    initialCustomer.setBalance(5000); // Initial balance

	    // Create a Customer object with the expected balance after deposit
	    Customer expectedCustomer = new Customer();
	    expectedCustomer.setId(1);
	    expectedCustomer.setBalance(5500); // Expected balance after deposit

	    // Convert the expectedCustomer to JSON
	    ObjectMapper objectMapper = new ObjectMapper();
	    String customerJson = objectMapper.writeValueAsString(expectedCustomer);

	    // Mock the behavior of the customerService.deposit() method
	    when(customerService.deposit(1, 500)).thenReturn(ResponseEntity.ok(expectedCustomer));

	    // Perform the PUT request to the /cust/deposit/1/500 endpoint
	    mockMvc.perform(put("/cust/deposit/1/500")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(customerJson));

	    // Verify that the deposit method of customerService was called with the correct parameters
	    verify(customerService).deposit(1, 500);
	}

	@Test
	void testWithdraw() throws Exception {
	    // Create a Customer object with the desired initial balance
	    Customer initialCustomer = new Customer();
	    initialCustomer.setId(1);
	    initialCustomer.setBalance(5000); // Initial balance

	    // Create a Customer object with the expected balance after withdrawal
	    Customer expectedCustomer = new Customer();
	    expectedCustomer.setId(1);
	    expectedCustomer.setBalance(4500); // Expected balance after withdrawal

	    // Convert the expectedCustomer to JSON
	    ObjectMapper objectMapper = new ObjectMapper();
	    String customerJson = objectMapper.writeValueAsString(expectedCustomer);

	    // Mock the behavior of the customerService.withdraw() method
	    when(customerService.withdraw(1, 500)).thenReturn(ResponseEntity.ok(expectedCustomer));

	    // Perform the PUT request to the /cust/withdraw/1/4500 endpoint
	    mockMvc.perform(put("/cust/withdraw/1/500")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(customerJson));

	    // Verify that the withdraw method of customerService was called with the correct parameters
	    verify(customerService).withdraw(1, 500);
	}
	  @Test
	    void testTransfer() throws Exception {
	        int id1 = 1;
	        int id2 = 2;
	        double amount = 500.0;

	        // Perform the PUT request to the endpoint
	        mockMvc.perform(put("/cust/transfer/{id1}/{id2}/{amount}", id1, id2, amount))
	                .andExpect(status().isOk());

	        // Verify that the service method is called with the correct parameters
	        verify(customerService).transfer(id1, id2, amount);
	    }

	
	

}
