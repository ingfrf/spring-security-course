package com.enmivida.app.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
@Entity
@Table(name = "customers")
@Data
public class CustomerEntity implements Serializable {
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Column(name = "email")
    private String email;
    @Column(name = "pwd")
    private String password;
    @Column(name = "rol")
    private String role;
}
