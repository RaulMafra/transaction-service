package com.transaction.service.service;

import com.transaction.service.domain.user.Client;
import com.transaction.service.dtos.request.UserDTO;
import com.transaction.service.exception.exceptions.IllegalFieldException;
import com.transaction.service.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
class ClientServiceTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Obtain a client created with successfully")
    void should_create_and_obtain_a_client_with_successfully() {
        String document = "11111111100";
        UserDTO client = new UserDTO("Maria", document, "maria@gmail.com", new BigDecimal(80));

        Client newClient = new Client(client);
        assertDoesNotThrow(() -> this.docFormatting(newClient));
        this.saveClient(newClient);

        assertThat(this.clientRepository.findClientByDocument("111.111.111-00").isPresent()).isTrue();

    }

    @Test
    @DisplayName("Failed in the creation of the client")
    void should_failed_to_the_create_and_obtain_a_client() {
        String document = "9999999999900";
        UserDTO client = new UserDTO("Jose", document, "jose@gmail.com", new BigDecimal(80));

        Client newClient = new Client(client);
        assertThrows(IllegalFieldException.class, () -> this.docFormatting(newClient));
        this.saveClient(newClient);

        assertThat(clientRepository.findClientByDocument("999.999.999-9900").isEmpty()).isTrue();

    }

    @DisplayName("Verify the amount of the characters of document and format this document")
    private void docFormatting(Client client) {
        if (client.getDocument().length() != 11) {
            throw new IllegalFieldException("A quantidade de caracteres do documento excede o permitido");
        }
        String docFormatted = String.format("%s.%s.%s-%s",
                client.getDocument().subSequence(0, 3),client.getDocument().subSequence(3,6),
                client.getDocument().subSequence(6,9),client.getDocument().subSequence(9,11));
        client.setDocument(docFormatted);
    }

    @DisplayName("Save a client with the correct data")
    public void saveClient(Client client) {
        this.entityManager.persist(client);
    }


}