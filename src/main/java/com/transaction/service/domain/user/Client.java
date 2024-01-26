package com.transaction.service.domain.user;

import com.transaction.service.dtos.request.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_client")
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "document", unique = true, nullable = false, length = 15)
    private String document;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;


    public Client(UserDTO userDTO){
        this.name = userDTO.name();
        this.document = userDTO.document();
        this.email = userDTO.email();
        this.balance = userDTO.balance();
    }

}
