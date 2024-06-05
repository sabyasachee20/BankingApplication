package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.model.Customer;
import com.example.repo.CustomerRepository;
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
		@Mock
		private CustomerRepository customerRepository;
		
		@InjectMocks
		private CustomerService customerService;
		
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
		void testRead() {
			List<Customer> custs=Arrays.asList(c1,c2);
			when(customerRepository.findAll()).thenReturn(custs);
			
			List<Customer>result=customerService.read();
			assertEquals(2,result.size());
			assertEquals("ashwini",result.get(0).getName());
			assertEquals("chandra",result.get(1).getName());
		}
		
		@Test
		void testReadOne(){
			when(customerRepository.findById(1)).thenReturn(Optional.of(c1));
			
			ResponseEntity<Customer> result=customerService.readOne(1);
			assertEquals(HttpStatus.OK, result.getStatusCode());
	        assertEquals("ashwini", result.getBody().getName());
		}
		@Test
		void testAdd() {
			customerService.post(c1);
			verify(customerRepository).save(c1);
		}
		@Test
		void testUpdate() {
			Customer updatedCust=new Customer();
			updatedCust.setName("Hemasri");
			updatedCust.setUsername("hema");
			updatedCust.setPassword("1234");
			updatedCust.setBalance(200000);
			when(customerRepository.findById(1)).thenReturn(Optional.of(c1));
			
			customerService.update(updatedCust, 1);
			assertEquals("Hemasri",c1.getName());
//			assertEquals("updated captain a",team1.getCaptian());
//			assertEquals(false,team2.isExChampion());
//			assertEquals("updated coach a",team1.getCoach());
			verify(customerRepository).save(c1);
			
		}
		
		@Test
		void testDelete() {
			when(customerRepository.findById(1)).thenReturn(Optional.of(c1));
			 ResponseEntity<Customer> response=customerService.delete(1);
			//verify(customerRepository).deleteById(1);
			 assertEquals(HttpStatus.OK, response.getStatusCode());
		}
		
		@Test
		void testDeposit() {
			Customer updatedCust=new Customer();
			updatedCust.setName("Hemasri");
			updatedCust.setUsername("hema");
			updatedCust.setPassword("1234");
			updatedCust.setBalance(200000);
			  when(customerRepository.findById(1)).thenReturn(Optional.of(c1));
			   // when(customerRepository.save(customer)).thenReturn(customer);

			    // Call the deposit method
			    ResponseEntity<Customer> response = customerService.deposit(1,500);

			    // Verify that the customer's balance is updated correctly
			    assertEquals(5500, c1.getBalance());

			    // Verify that save method of CustomerRepository is called
			  //  verify(customerRepository).save(customer);

			    // Assert that the response has HTTP status OK
			    assertEquals(HttpStatus.OK, response.getStatusCode());
			
		}
		@Test
		void testWithdraw() {
			  when(customerRepository.findById(1)).thenReturn(Optional.of(c1));
			   // when(customerRepository.save(customer)).thenReturn(customer);

			    // Call the deposit method
			    ResponseEntity<Customer> response = customerService.withdraw(1,500);

			    // Verify that the customer's balance is updated correctly
			    assertEquals(4500, c1.getBalance());

			    // Verify that save method of CustomerRepository is called
			  //  verify(customerRepository).save(customer);

			    // Assert that the response has HTTP status OK
			    assertEquals(HttpStatus.OK, response.getStatusCode());
			
		}
		@Test
		void testTransfer() {
			  when(customerRepository.findById(1)).thenReturn(Optional.of(c1));
			   // when(customerRepository.save(customer)).thenReturn(customer);
			  when(customerRepository.findById(2)).thenReturn(Optional.of(c2));
			    // Call the deposit method
			    customerService.transfer(1,2,500);

			    // Verify that the customer's balance is updated correctly
			    assertEquals(4500, c1.getBalance());
			    assertEquals(5500, c2.getBalance());

			    // Verify that save method of CustomerRepository is called
			  //  verify(customerRepository).save(customer);

			    // Assert that the response has HTTP status OK
			   // assertEquals(HttpStatus.OK, response.getStatusCode());
			
		}

	}
