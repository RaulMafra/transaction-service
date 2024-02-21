package com.transaction.service.service;

import com.transaction.service.domain.user.Client;
import com.transaction.service.dtos.request.TransactionDTO;
import com.transaction.service.dtos.request.UserDTO;
import com.transaction.service.exception.exceptions.IllegalFieldException;
import com.transaction.service.exception.exceptions.RestException;
import com.transaction.service.exception.exceptions.UserNotFound;
import com.transaction.service.repository.ClientRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    private ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void createClient(UserDTO client) {
        if (Stream.of(client.document(), client.name(), client.balance(), client.email()).anyMatch(Objects::isNull)) {
            throw new IllegalFieldException("There's some value absent in the body");
        }
        Client newClient = new Client(client);
        this.docFormatting(newClient);
        this.saveClient(newClient);
    }

    private void docFormatting(Client client) {
        if (client.getDocument().length() != 11) {
            throw new IllegalFieldException("Quantity of characters of the document insufficient or exceeds the allowed");
        }
        String docFormatted = String.format("%s.%s.%s-%s",
                client.getDocument().subSequence(0, 3), client.getDocument().subSequence(3, 6),
                client.getDocument().subSequence(6, 9), client.getDocument().subSequence(9, 11));
        client.setDocument(docFormatted);
    }

    public Client getClient(TransactionDTO transactionDTO) {
        return clientRepository.findClientById(transactionDTO.idClient()).orElseThrow(() -> new UserNotFound("Client not found"));
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    public List<Client> listAllClients() {
        return clientRepository.findAll();
    }
}
