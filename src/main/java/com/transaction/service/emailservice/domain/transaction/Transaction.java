package com.transaction.service.emailservice.domain.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.transaction.service.emailservice.domain.user.Client;
import com.transaction.service.emailservice.domain.user.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Long id;
    @Column(name = "transactionValue")
    private BigDecimal value;
    @Column(name = "tax")
    private BigDecimal tax;
    @ManyToOne
    @JoinColumns(value = @JoinColumn(name = "id_client"), foreignKey = @ForeignKey(name = "id_client"))
    private Client client;
    @ManyToOne
    @JoinColumn(name = "id_company")
    private Company company;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(BigDecimal value, BigDecimal tax, Client client, Company company, TransactionType transactionType){
        this.value = value;
        this.tax = tax;
        this.client = client;
        this.company = company;
        this.timestamp = LocalDateTime.now();
        this.transactionType = transactionType;
    }

}
