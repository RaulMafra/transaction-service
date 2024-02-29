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
        newClient.setDocument(this.docFormatting(newClient.getDocument()));
        this.saveClient(newClient);
    }

    private String docFormatting(String document) {
        if (document.length() != 11) {
            throw new IllegalFieldException("Quantity of characters of the document insufficient or exceeds the allowed");
        }
        return String.format("%s.%s.%s-%s",
                document.subSequence(0, 3), document.subSequence(3, 6),
                document.subSequence(6, 9), document.subSequence(9, 11));
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
