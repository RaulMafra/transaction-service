package com.transaction.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.service.domain.user.Company;
import com.transaction.service.dtos.request.CreateUserDTO;
import com.transaction.service.service.CompanyService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
class CompanyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    private URI uri;

    @SneakyThrows
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(companyController)
                .alwaysDo(MockMvcResultHandlers.print()).build();

        uri = new URI("/restservice/v1/companies");
    }

    @SneakyThrows
    @Test
    void should_create_a_company_and_return_status_code_201() {
        CreateUserDTO company = new CreateUserDTO("Example", "34345678000121",
                "example@email.com", new BigDecimal(100));

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(new ObjectMapper().writeValueAsString(company)))
                .andExpectAll(status().isCreated(),
                        content().string("{\"message\":\"Created company with successfully\"}"))
                .andReturn();

        verify(this.companyService, Mockito.times(1)).createCompany(company);
        verifyNoMoreInteractions(this.companyService);
    }

    @SneakyThrows
    @Test
    void should_return_a_list_of_the_companies_and_status_code_200() {
        Company company = new Company(1L, "Example", "34345678000121", "example@test.com", new BigDecimal(0));
        List<Company> companies = new ArrayList<>(List.of(company));

        when(this.companyService.listAllCompanies()).thenReturn(companies);

        mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(new ObjectMapper().writeValueAsString(company)))
                .andExpectAll(status().isOk(),
                        content().string(new ObjectMapper().writeValueAsString(companies)))
                .andReturn();

        verify(this.companyService, times(1)).listAllCompanies();
        verifyNoMoreInteractions(this.companyService);

    }
}