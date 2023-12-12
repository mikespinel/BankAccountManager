package com.aitho.contocorrente.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.Set;

@Table(name = "customer")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "tax_code")
    private String taxCode;

    //@Transient
    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties("transactions")
    //@ToString.Exclude
    private Set<BankAccount> bankAccounts;


}
