package org.example.dev_320_network_client_data_storage_system_rest.service;

import jakarta.validation.Valid;
import org.example.dev_320_network_client_data_storage_system_rest.dto.AddressDto;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.AddressEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AddressService {
    AddressDto create (@Valid AddressDto dto);
    AddressDto update (@Valid AddressDto dto);

}
