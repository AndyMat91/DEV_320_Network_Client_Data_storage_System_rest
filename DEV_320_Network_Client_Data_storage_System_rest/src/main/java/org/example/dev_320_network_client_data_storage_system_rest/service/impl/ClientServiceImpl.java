package org.example.dev_320_network_client_data_storage_system_rest.service.impl;

import org.example.dev_320_network_client_data_storage_system_rest.dto.ClientDto;
import org.example.dev_320_network_client_data_storage_system_rest.enums.CLIENT_TYPE;
import org.example.dev_320_network_client_data_storage_system_rest.repository.ClientRepository;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.ClientEntity;
import org.example.dev_320_network_client_data_storage_system_rest.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDto create(ClientDto dto){
        dto.setClientId(null);
        return clientRepository.create(dtoToEntity(dto))
                .map(this::entityToDto).orElse(null);
    }

    public ClientDto update (ClientDto dto){
        return clientRepository.update(dtoToEntity(dto))
                .map(this::entityToDto).orElse(null);
    }

    public ClientEntity dtoToEntity (ClientDto dto){
        ClientEntity p = new ClientEntity();
        p.setClientId(dto.getClientId());
        p.setClientType(dto.getClientType().getValue());
        p.setClientName(dto.getClientName());
        p.setDatereg(dto.getDatereg());
        p.setAddresses(dto.getAddresses());
        return p;
    }

    public ClientDto entityToDto(ClientEntity entity) {
        return ClientDto.builder()
                .clientId(entity.getClientId())
                .clientType(CLIENT_TYPE.getType(entity.getClientType()))
                .clientName(entity.getClientName())
                .datereg(entity.getDatereg())
                .addresses(entity.getAddresses())
                .build();
    }

}
