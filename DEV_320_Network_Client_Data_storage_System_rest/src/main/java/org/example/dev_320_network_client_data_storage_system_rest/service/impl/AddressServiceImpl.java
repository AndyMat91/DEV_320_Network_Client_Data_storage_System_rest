package org.example.dev_320_network_client_data_storage_system_rest.service.impl;

import org.example.dev_320_network_client_data_storage_system_rest.dto.AddressDto;
import org.example.dev_320_network_client_data_storage_system_rest.dto.ClientDto;
import org.example.dev_320_network_client_data_storage_system_rest.enums.CLIENT_TYPE;
import org.example.dev_320_network_client_data_storage_system_rest.repository.AddressRepository;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.AddressEntity;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.ClientEntity;
import org.example.dev_320_network_client_data_storage_system_rest.service.AddressService;
import org.example.dev_320_network_client_data_storage_system_rest.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private ClientServiceImpl clientService;

    public AddressServiceImpl(AddressRepository addressRepository, ClientServiceImpl clientService) {
        this.addressRepository = addressRepository;
        this.clientService = clientService;
    }

    public AddressDto create(AddressDto dto){
        return addressRepository.create(dtoToEntity(dto))
                .map(this::entityToDto).orElse(null);
    }

    public AddressDto update(AddressDto dto){
        return addressRepository.update(dtoToEntity(dto))
                .map(this::entityToDto).orElse(null);
    }

    public AddressEntity dtoToEntity (AddressDto dto){
        AddressEntity p = new AddressEntity();
        p.setClient(clientService.dtoToEntity(dto.getClientDto()));
        p.setAddress(dto.getAddress());
        p.setModel(dto.getModel());
        p.setIp(dto.getIp());
        p.setMac(dto.getMac());
        return p;
    }

    public AddressDto entityToDto(AddressEntity entity) {
        return AddressDto.builder()
                .ip(entity.getIp())
                .mac(entity.getMac())
                .address(entity.getAddress())
                .model(entity.getModel())
                .clientDto(clientService.entityToDto(entity.getClient()))
                .build();
    }
}

