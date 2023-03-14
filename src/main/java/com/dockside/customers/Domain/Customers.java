package com.dockside.customers.Domain;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private UUID user_id;
    private String first_name;
    private String last_name;
    private String location;
    private String default_payment;
    @Column(name = "phone_number")
    private String phone;
    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

}
