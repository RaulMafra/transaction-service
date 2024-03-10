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
    void should_create_and_obtain_a_client_with_successfully() {
        UserDTO client = new UserDTO("Test", "11111111100", "test@example.com", new BigDecimal(80));

        Client newClient = new Client(client);
        assertDoesNotThrow(() -> newClient.setDocument(this.docFormatting(newClient.getDocument())));
        this.saveClient(newClient);

        assertThat(this.clientRepository.findClientByDocument("111.111.111-00").isPresent()).isTrue();

    }

    @Test
    void should_failed_to_the_create_a_client_when_size_document_different_of_the_11() {
        UserDTO client = new UserDTO("Test", "9999999999900", "test@example.com", new BigDecimal(80));

        Client newClient = new Client(client);
        assertThrows(IllegalFieldException.class, () -> newClient.setDocument(this.docFormatting(newClient.getDocument())),
                "Quantity of characters of the document insufficient or exceeds the allowed");
        this.saveClient(newClient);

        assertThat(clientRepository.findClientByDocument("999.999.999-9900").isEmpty()).isTrue();

    }
    

    @Test
    void should_search_with_successfully_all_clients_created() {
        UserDTO client = new UserDTO("Test", "11111111100", "test@example.com", new BigDecimal(80));
        UserDTO client2 = new UserDTO("Test2", "22111111100", "test2@example.com", new BigDecimal(50));

        Client newClient = new Client(client);
        Client newClient2 = new Client(client2);

        assertDoesNotThrow(() -> newClient.setDocument(this.docFormatting(newClient.getDocument())));
        assertDoesNotThrow(() -> newClient2.setDocument(this.docFormatting(newClient2.getDocument())));

        this.saveClient(newClient);
        this.saveClient(newClient2);

        assertThat(this.clientRepository.findAll()).isNotEmpty();

    }

    @DisplayName("Verify the amount of the characters of document and format this document")
    String docFormatting(String document) {
        if (document.length() != 11) {
            throw new IllegalFieldException("Quantity of characters of the document insufficient or exceeds the allowed");
        }
        return String.format("%s.%s.%s-%s",
                document.subSequence(0, 3), document.subSequence(3, 6),
                document.subSequence(6, 9), document.subSequence(9, 11));
    }

    @DisplayName("Save a client with the correct data")
    void saveClient(Client client) {
        this.entityManager.persist(client);
    }


}