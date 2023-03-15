package com.dockside.customers.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dockside.customers.Domain.Customers;
import com.dockside.customers.repositories.CustomersRepository;

@ExtendWith(MockitoExtension.class)
public class CustomersServiceImplTest {

    @Mock
    private CustomersRepository customerRepo;

    @InjectMocks
    private CustomersServiceImpl customersService;

    public Customers testCustomer() {
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
    void createCustomerAccount_ShouldReturnCustomer() {
        // Given
        Customers customer = new Customers();
        customer.setFirst_name("John");
        customer.setLast_name("Doe");
        customer.setLocation("Test");
        customer.setPhone("01234556");
        customer.setDefault_payment("TestPay");
        customer.setId(Long.valueOf(1));

        Mockito.when(customerRepo.save(Mockito.any(Customers.class))).thenReturn(customer);

        // When
        Customers savedCustomer = customersService.createCustomerAccount(customer);

        // Then
        assertEquals(customer, savedCustomer);
    }

    @Test
    void createCustomerAccount_ShouldSaveCustomer() {
        Customers customer = new Customers();
        customer.setFirst_name("John");
        customer.setLast_name("Doe");
        customer.setLocation("Test");
        customer.setPhone("01234556");
        customer.setDefault_payment("TestPay");
        customer.setId(1L);
        Customers savedCustomer = new Customers();
        savedCustomer.setId(1L);
        savedCustomer.setFirst_name("John");
        savedCustomer.setLast_name("Doe");
        savedCustomer.setLocation("Test");
        savedCustomer.setPhone("01234556");
        savedCustomer.setDefault_payment("TestPay");

        Mockito.when(customerRepo.save(customer)).thenReturn(savedCustomer);

        Customers result = customersService.createCustomerAccount(customer);

        Mockito.verify(customerRepo, Mockito.times(1)).save(customer);

        assertEquals(savedCustomer.getId(), result.getId());
        assertEquals(savedCustomer.getFirst_name(), result.getFirst_name());
        assertEquals(savedCustomer.getLast_name(), result.getLast_name());
        assertEquals(savedCustomer.getLocation(), result.getLocation());
        assertEquals(savedCustomer.getPhone(), result.getPhone());
        assertEquals(savedCustomer.getDefault_payment(), result.getDefault_payment());
    }

    @Test
    void getAllCustomers_ShouldReturnListOfCustomers() {
        Customers customer1 = new Customers();
        customer1.setId(1L);
        customer1.setFirst_name("John");
        customer1.setLast_name("Doe");
        customer1.setLocation("Test");
        customer1.setPhone("01234556");
        customer1.setDefault_payment("TestPay");

        Customers customer2 = new Customers();
        customer2.setId(2L);
        customer2.setFirst_name("Jane");
        customer2.setLast_name("Doe");
        customer2.setLocation("Test");
        customer2.setPhone("01234557");
        customer2.setDefault_payment("TestPay");

        List<Customers> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        Mockito.when(customerRepo.findAll()).thenReturn(customers);

        List<Customers> result = customersService.getAllCustomers();

        Mockito.verify(customerRepo, Mockito.times(1)).findAll();

        assertEquals(customers.size(), result.size());
        assertEquals(customers.get(0).getId(), result.get(0).getId());
        assertEquals(customers.get(0).getFirst_name(), result.get(0).getFirst_name());
        assertEquals(customers.get(0).getLast_name(), result.get(0).getLast_name());
        assertEquals(customers.get(0).getLocation(), result.get(0).getLocation());
        assertEquals(customers.get(0).getPhone(), result.get(0).getPhone());
        assertEquals(customers.get(0).getDefault_payment(), result.get(0).getDefault_payment());
        assertEquals(customers.get(1).getId(), result.get(1).getId());
        assertEquals(customers.get(1).getFirst_name(), result.get(1).getFirst_name());
        assertEquals(customers.get(1).getLast_name(), result.get(1).getLast_name());
        assertEquals(customers.get(1).getLocation(), result.get(1).getLocation());
        assertEquals(customers.get(1).getPhone(), result.get(1).getPhone());
        assertEquals(customers.get(1).getDefault_payment(), result.get(1).getDefault_payment());
    }

    @Test
    void testGetAllCustomers() {
        Customers customer1 = testCustomer();
        Customers customer2 = testCustomer();
        customer2.setId(2L);
        Mockito.when(customerRepo.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        List<Customers> customersList = customersService.getAllCustomers();
        assertEquals(2, customersList.size());
    }

    @Test
    void testGetCustomerById_whenCustomerExists() {
        Customers customer = testCustomer();
        Mockito.when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        Optional<Customers> result = customersService.getCustomerById(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirst_name());
    }

    @Test
    void testGetCustomerById_whenCustomerDoesNotExist() {
        Mockito.when(customerRepo.findById(1L)).thenReturn(Optional.empty());
        Optional<Customers> result = customersService.getCustomerById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetCustomerByPhone_whenCustomerExists() {
        Customers customer = testCustomer();
        Mockito.when(customerRepo.findCustomerByPhone("01234556")).thenReturn(Optional.of(customer));
        Optional<Customers> result = customersService.getCustomerByPhone("01234556");
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirst_name());
    }

    @Test
    void testGetCustomerByPhone_whenCustomerDoesNotExist() {
        Mockito.when(customerRepo.findCustomerByPhone("01234556")).thenReturn(Optional.empty());
        Optional<Customers> result = customersService.getCustomerByPhone("01234556");
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateDetails() {
        Customers customer = testCustomer();
        Mockito.when(customerRepo.save(Mockito.any(Customers.class))).thenReturn(customer);
        Customers updatedCustomer = customersService.updateDetails(customer);
        assertEquals("John", updatedCustomer.getFirst_name());
    }

    @Test
    void testDeleteUserById() {
        customersService.deleteUserById(1L);
        Mockito.verify(customerRepo, Mockito.times(1)).deleteById(1L);
    }
}