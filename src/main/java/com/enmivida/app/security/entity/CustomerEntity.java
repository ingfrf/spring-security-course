package com.enmivida.app.security.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
public class CustomerEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "email")
    private String email;
    @Column(name = "pwd")
    private String password;
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="id_customer")
    private List<RoleEntity> roles;
}
