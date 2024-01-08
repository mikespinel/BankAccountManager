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
public class Customer extends User{

    public Customer(String firstName, String lastName, String taxCode, String username, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.taxCode = taxCode;
        super.setUsername(username);
        super.setEmail(email);
        super.setPassword(password);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "tax_code", unique = true)
    private String taxCode;

    //@Transient
    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties("transactions")
    //@ToString.Exclude
    private Set<BankAccount> bankAccounts;


}
