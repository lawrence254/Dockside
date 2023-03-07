package com.dockside.customers.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dockside.customers.Domain.Customers;
import com.dockside.customers.repositories.CustomersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomersServiceImpl implements CustomersService {
    private final CustomersRepository customerRepo;

    @Override
    public Customers createCustomerAccount(Customers customer) {
        log.info("Creating Customer Account");
        return customerRepo.save(customer);
    }

    @Override
    public List<Customers> getAllCustomers() {
        log.info("Fetching all Customer Accounts");
        return customerRepo.findAll();
    }

    @Override
    public Optional<Customers> getCustomerById(Long id) {
        log.info("Searching for the customer with id {}", id);
        return customerRepo.findById(id);
    }

    @Override
    public Optional<Customers> getCustomerByPhone(String phone_number) {
        log.info("Searching for customer with Phone Number: {}", phone_number);
        return customerRepo.findCustomerByPhone(phone_number);
    }
}
