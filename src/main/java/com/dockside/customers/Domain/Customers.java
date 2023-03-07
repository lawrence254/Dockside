package com.dockside.customers.Domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    private String email_address;
    private String location;
    private String default_payment;
    @Column(name = "phone_number")
    private String phone;
    private Date created_at;
    private Date updated_at;

}
