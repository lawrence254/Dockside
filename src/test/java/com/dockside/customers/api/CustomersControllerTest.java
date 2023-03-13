package com.dockside.customers.api;

import com.dockside.customers.services.CustomersService;
import com.dockside.customers.Domain.Customers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith(MockitoExtension.class)
class CustomersControllerTest {
    @Mock
    private CustomersService customerService;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CustomersController customersController;


    public Customers testCustomer(){
        Customers customer = new Customers();
        var testId = UUID.fromString("b7f2f9ca-5b66-4228-9028-90767698163c");
        customer.setFirst_name("John");
        customer.setLast_name("Doe");
        customer.setUser_id(testId);
        customer.setLocation("Test");
        customer.setPhone("01234556");
        customer.setDefault_payment("TestPay");
        customer.setId(Long.valueOf(1));

        return customer;
    }

    @Test
    void createCustomerAccountTest() {
        // Create a new customer
        Customers customer = testCustomer();
        // Mock the customerService's createCustomerAccount method
        when(customerService.createCustomerAccount(customer)).thenReturn(customer);
        // Call the controller method
        ResponseEntity<Customers> response = customersController.createCustomerAccount(customer);
        // Check that the response status code is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Check that the response body is the same as the customer object we created
        assertEquals(customer, response.getBody());
    }

    @Test
    void getAllCustomerAccountsTest() {
        // Create a list of customers
        List<Customers> customers = new ArrayList<>();
        customers.add(testCustomer());
        customers.add(testCustomer());
        // Mock the customerService's getAllCustomers method
        when(customerService.getAllCustomers()).thenReturn(customers);
        // Call the controller method
        ResponseEntity<List<Customers>> response = customersController.getAllCustomerAccounts();
        // Check that the response status code is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Check that the response body is the same as the list of customers we created
        assertEquals(customers, response.getBody());
    }

    @Test
    void getCustomersByPhoneTest() {
        // Create a customer with a phone number
        Customers customer = testCustomer();
        // Mock the customerService's getCustomerByPhone method
        when(customerService.getCustomerByPhone(customer.getPhone())).thenReturn(Optional.of(customer));
        // Call the controller method
        ResponseEntity<Optional<Customers>> response = customersController.getCustomersByPhone(customer.getPhone());
        // Check that the response status code is OK (200)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Check that the response body is the same as the customer object we created
        assertEquals(customer, response.getBody().get());
    }


    @Test
    public void updateCustomerDetails_Successful() throws JsonMappingException {
        String phone = "1234567890";
        Map<String, Object> details = new HashMap<>();
        details.put("first_name", "John Doe");

        Customers customer = testCustomer();

        Customers updatedCustomer = testCustomer();
        updatedCustomer.setFirst_name("John Doe");

        when(customerService.getCustomerByPhone(phone)).thenReturn(Optional.of(customer));
        when(objectMapper.updateValue(customer, details)).thenReturn(updatedCustomer);
        when(customerService.updateDetails(updatedCustomer)).thenReturn(updatedCustomer);

        ResponseEntity<?> response = customersController.updateCustomerDetails(phone, details);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomer, response.getBody());
    }

    @Test
    public void updateCustomerDetails_Failed() throws JsonMappingException {
        String phone = "1234567890";
        Map<String, Object> details = new HashMap<>();
        details.put("last_name", "John Doe");

        Customers customer = testCustomer();
        when(customerService.getCustomerByPhone(phone)).thenReturn(Optional.empty());

        ResponseEntity<?> response = customersController.updateCustomerDetails(phone, details);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found with phone number: " + phone, response.getBody());
    }


    @Test
    public void deleteUserById_Successful() {
        String userId = "1";
        doNothing().when(customerService).deleteUserById(Long.parseLong(userId));
        ResponseEntity<?> response = customersController.deleteUserById(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Deleted Customer Successfully", response.getBody());
    }

    @Test
    public void deleteUserById_Failed() {
        String userId = "123";
        String errorMessage = "Error deleting user";
        doThrow(new RuntimeException(errorMessage)).when(customerService).deleteUserById(Long.parseLong(userId));
        ResponseEntity<?> response = customersController.deleteUserById(userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains(errorMessage));
    }
}