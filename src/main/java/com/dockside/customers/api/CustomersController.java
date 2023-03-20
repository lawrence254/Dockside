package com.dockside.customers.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dockside.customers.Domain.Customers;
import com.dockside.customers.services.CustomersService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomersController {
    public final CustomersService customerService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Customers> createCustomerAccount(@RequestBody Customers customer) {
        return ResponseEntity.ok().body(customerService.createCustomerAccount(customer));
    }

    @GetMapping
    public ResponseEntity<List<Customers>> getAllCustomerAccounts() {
        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @GetMapping("{phone}")
    public ResponseEntity<Optional<Customers>> getCustomersByPhone(@PathVariable String phone) {
        return ResponseEntity.ok().body(customerService.getCustomerByPhone(phone));
    }

    @PatchMapping("{phone}")
    public ResponseEntity<?> updateCustomerDetails(@PathVariable String phone, @RequestBody Map<String, Object> details) throws JsonMappingException{
        Customers user = customerService.getCustomerByPhone(phone).get();
        var updatedFields = objectMapper.updateValue(user, details);
        return ResponseEntity.ok().body(customerService.updateDetails(updatedFields));
    }

    @DeleteMapping("{userid}")
    public ResponseEntity<?> deleteUserById(@PathVariable String userid){
        try{
            customerService.deleteUserById(Long.parseLong(userid));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete user due to error: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Customer Successfully");
    }

}
