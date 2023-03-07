package com.dockside.customers.services;
import com.dockside.customers.Domain.Customers;
import java.util.List;
import java.util.Optional;
public interface CustomersService{
    Customers createCustomerAccount(Customers customer);
    List<Customers> getAllCustomers();
    Optional<Customers> getCustomerByPhone(String phone_number);
    Optional<Customers> getCustomerById(Long id);
}