package com.safeway.test.service;

import com.safeway.test.domain.user.Client;
import com.safeway.test.dtos.ClientDTO;
import com.safeway.test.dtos.TransactionDTO;
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

    public void createClient(ClientDTO clientDTO) {
        Client newClient = new Client(clientDTO);
        this.checkFields(newClient);
        this.saveClient(newClient);
    }

    private void checkFields(Client client) {
        if (client.getDocument().length() != 11) {
            throw new RuntimeException("O CPF deve ter 11 dígitos!");
        }
    }

    public Client getClient(TransactionDTO transactionDTO){
        return clientRepository.findClientById(transactionDTO.idClient()).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public void saveClient(Client client){
        clientRepository.save(client);
    }

    public List<Client> listAllClients(){
        return clientRepository.findAll();
    }
}
