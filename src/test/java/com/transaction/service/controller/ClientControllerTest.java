package com.transaction.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.service.domain.user.Client;
import com.transaction.service.dtos.request.CreateUserDTO;
import com.transaction.service.service.ClientService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

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
    void should_create_a_client_and_return_status_code_201() {
        CreateUserDTO client = new CreateUserDTO("Example", "12356678123",
                "example@email.com", new BigDecimal(100));

        doNothing().when(clientService).createClient(client);

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(new ObjectMapper().writeValueAsString(client)))
                .andExpectAll(status().isCreated(),
                        content().string("{\"message\":\"Created client with successfully\"}"))
                .andReturn();

        verify(clientService, times(1)).createClient(client);
        verifyNoMoreInteractions(clientService);
    }

    @SneakyThrows
    @Test
    void should_return_a_list_of_the_clients_and_status_code_200() {
        Client client = new Client(1L, "example", "11111111100", "example@teste.com", new BigDecimal(0));
        List<Client> clients = new ArrayList<>(List.of(client));

        when(this.clientService.listAllClients()).thenReturn(clients);

        mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(new ObjectMapper().writeValueAsString(clients)))
                .andExpectAll(status().isOk(),
                        content().string(new ObjectMapper().writeValueAsString(clients)))
                .andReturn();

        verify(this.clientService, times(1)).listAllClients();
        verifyNoMoreInteractions(clientService);
    }
}