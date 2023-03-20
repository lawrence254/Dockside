package com.dockside.customers.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dockside.customers.Domain.Customers;
import com.dockside.customers.repositories.CustomersRepository;
import com.dockside.customers.services.CustomersService;

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
        return customerRepo.save(customer);
    }

    @Override
    public List<Customers> getAllCustomers() {
        return customerRepo.findAll();
    }

    @Override
    public Optional<Customers> getCustomerById(Long id) {
        return customerRepo.findById(id);
    }

    @Override
    public Optional<Customers> getCustomerByPhone(String phone_number) {
        return customerRepo.findCustomerByPhone(phone_number);
    }

    @Override
    public Customers updateDetails(Customers customer){
        return customerRepo.save(customer);
    }

    @Override
    public void deleteUserById(Long id){
        customerRepo.deleteById(id);
    }
}
