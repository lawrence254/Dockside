package com.dockside.customers.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dockside.customers.Domain.Customers;
import java.util.Optional;

public interface CustomersRepository extends JpaRepository<Customers, Long> {
    Optional<Customers> findCustomerByPhone(String phone_number);    
}
