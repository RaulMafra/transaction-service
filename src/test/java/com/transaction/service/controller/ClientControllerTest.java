package com.transaction.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.service.domain.user.Client;
import com.transaction.service.dtos.request.UserDTO;
import com.transaction.service.service.ClientService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private String serializeObject;

    private URI uri;

    @SneakyThrows
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .alwaysDo(MockMvcResultHandlers.print()).build();

        uri = new URI("/restservice/v1/clients");
    }

    @SneakyThrows
    @Test
    @DisplayName("Create a client with successfully and return status code 201")
    void should_create_a_client_and_return_status_code_201(){
        UserDTO client = new UserDTO("Example", "12356678123",
                "example@email.com", new BigDecimal(100));
        serializeObject = new ObjectMapper().writeValueAsString(client);

        doNothing().when(clientService).createClient(client);

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                .content(serializeObject))
                .andExpect(status().isCreated()).andReturn();

        verify(clientService, times(1)).createClient(client);
        verifyNoMoreInteractions(clientService);
    }

    @SneakyThrows
    @Test
    @DisplayName("Search a list of the clients e return status code 200")
    void should_return_a_list_of_the_clients_and_status_code_200() throws JsonProcessingException {
        Client client1 = new Client(UUID.randomUUID(), "example", "11111111100", "example@teste.com", new BigDecimal(0));
        List<Client> clients = new ArrayList<>(List.of(client1));

        serializeObject = new ObjectMapper().writeValueAsString(clients);

        when(this.clientService.listAllClients()).thenReturn(Collections.singletonList(client1));

        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                        .content(serializeObject))
                .andExpect(status().isOk());

        verify(this.clientService, times(1)).listAllClients();
        verifyNoMoreInteractions(clientService);
    }
}