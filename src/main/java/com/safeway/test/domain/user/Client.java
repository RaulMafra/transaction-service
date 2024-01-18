package com.safeway.test.domain.user;

import com.safeway.test.dtos.ClientDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    protected Long id;
    @Column(name = "name", nullable = false)
    protected String name;
    @Column(name = "document", unique = true, nullable = false)
    protected String document;
    @Column(name = "email", unique = true, nullable = false)
    protected String email;
    @Column(name = "balance", nullable = false)
    protected BigDecimal balance;


    public Client(ClientDTO clientDTO){
        this.name = clientDTO.name();
        this.document = clientDTO.document();
        this.email = clientDTO.email();
        this.balance = clientDTO.balance();
    }

}
