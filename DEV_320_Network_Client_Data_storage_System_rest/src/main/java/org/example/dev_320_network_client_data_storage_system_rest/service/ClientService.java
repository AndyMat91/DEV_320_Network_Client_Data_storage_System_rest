package org.example.dev_320_network_client_data_storage_system_rest.service;

import jakarta.validation.Valid;
import org.example.dev_320_network_client_data_storage_system_rest.dto.ClientDto;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ClientService {
    ClientDto create (@Valid ClientDto dto);
    ClientDto update (@Valid ClientDto dto);
}
