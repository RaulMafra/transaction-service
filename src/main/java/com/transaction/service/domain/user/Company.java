package com.transaction.service.domain.user;

import com.transaction.service.dtos.request.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "document", unique = true, nullable = false, length = 18)
    private String document;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    public Company(UserDTO company){
        this.name = company.name();
        this.document = company.document();
        this.email = company.email();
        this.balance = company.balance();
    }
}
