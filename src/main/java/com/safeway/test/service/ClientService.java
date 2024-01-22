package com.safeway.test.service;

import com.safeway.test.domain.user.Client;
import com.safeway.test.dtos.TransactionDTO;
import com.safeway.test.dtos.UserDTO;
import com.safeway.test.exception.exceptions.IllegalFormattingException;
import com.safeway.test.exception.exceptions.RestException;
import com.safeway.test.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    private ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void createClient(UserDTO userDTO) {
        Client newClient = new Client(userDTO);
        this.docFormatting(newClient);
        this.saveClient(newClient);
    }

    private void docFormatting(Client client) {
        if (client.getDocument().length() != 11) {
            throw new IllegalFormattingException("A quantidade de caracteres do documento excede o permitido");
        }
        String docFormatted = String.format("%s.%s.%s-%s",
                client.getDocument().subSequence(0, 3),client.getDocument().subSequence(3,6),
                client.getDocument().subSequence(6,9),client.getDocument().subSequence(9,11));
        client.setDocument(docFormatted);
    }

    public Client getClient(TransactionDTO transactionDTO){
        return clientRepository.findClientById(transactionDTO.idClient()).orElseThrow(() -> new RestException("Cliente nao encontrado"));
    }

    public void saveClient(Client client){
        clientRepository.save(client);
    }

    public List<Client> listAllClients(){
        return clientRepository.findAll();
    }
}
