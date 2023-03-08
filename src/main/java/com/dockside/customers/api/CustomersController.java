package com.dockside.customers.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dockside.customers.Domain.Customers;
import com.dockside.customers.services.CustomersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomersController {
    public final CustomersService customerService;

    @PostMapping
    public ResponseEntity<Customers> createCustomerAccount(@RequestBody Customers customer) {
        // URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/customers").toUriString());
        return ResponseEntity.ok().body(customerService.createCustomerAccount(customer));
    }

    @GetMapping
    public ResponseEntity<List<Customers>> getAllCustomerAccounts() {
        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @GetMapping("{userid}")
    public ResponseEntity<Optional<Customers>> getCustomerById(@PathVariable Long userid) {
        return ResponseEntity.ok().body(customerService.getCustomerById(userid));
    }

    @GetMapping("{phone}")
    public ResponseEntity<Optional<Customers>> getCustomersByPhone(@PathVariable String phone) {
        return ResponseEntity.ok().body(customerService.getCustomerByPhone(phone));
    }

}
