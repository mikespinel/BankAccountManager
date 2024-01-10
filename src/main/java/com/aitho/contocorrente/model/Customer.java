package com.aitho.contocorrente.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Table(name = "customer")
@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Customer extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "tax_code", unique = true)
    private String taxCode;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties("transactions")
    private Set<BankAccount> bankAccounts;

}
