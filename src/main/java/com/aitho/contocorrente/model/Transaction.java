package com.aitho.contocorrente.model;

import javax.persistence.*;

import com.aitho.contocorrente.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Table(name = "transaction")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_time", nullable = false)
    private Date dateTime;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = false)
    @JsonIgnoreProperties({"customer", "transactions", "balance"})
    private BankAccount bankAccount;
}
