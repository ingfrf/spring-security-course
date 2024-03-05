package com.enmivida.app.security.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Table(name = "partner")
@Data
public class PartnerEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "client_secret")
    private String clientSecret;
    @Column(name = "scopes")
    private String scopes;
    @Column(name = "grant_types")
    private String grantTypes;
    @Column(name = " authentication_methods")
    private String authenticationMethods;
    @Column(name = "redirect_uri")
    private String redirectUri;
    @Column(name = "redirect_uri_logout")
    private String redirectUriLogout;
}
