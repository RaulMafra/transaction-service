package com.safeway.test.domain.user;

import com.safeway.test.dtos.UserDTO;
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
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "document", unique = true, nullable = false)
    private String document;
    @Column(name = "email", unique = true, nullable = false, length = 11)
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
