package org.example.dev_310_1_network_client_data_storage_system.service.impl;

import org.example.dev_310_1_network_client_data_storage_system.common.dto.AddressDto;
import org.example.dev_310_1_network_client_data_storage_system.common.dto.ClientDto;
import org.example.dev_310_1_network_client_data_storage_system.common.entity.AddressEntity;
import org.example.dev_310_1_network_client_data_storage_system.common.entity.ClientEntity;
import org.example.dev_310_1_network_client_data_storage_system.common.exceptions.EAppException;
import org.example.dev_310_1_network_client_data_storage_system.dto.ClientAddressDto;
import org.example.dev_310_1_network_client_data_storage_system.service.AddressService;
import org.example.dev_310_1_network_client_data_storage_system.service.ClientAddressService;
import org.example.dev_310_1_network_client_data_storage_system.service.ClientService;
import org.example.dev_310_1_network_client_data_storage_system.common.Converters;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ClientAddressServiceImpl implements ClientAddressService {
    private final ClientService clientService;
    private final AddressService addressService;
    private final WebClient webClient;


    public ClientAddressServiceImpl(
            @Qualifier(value = "clientServiceImpl") ClientService clientService,
            AddressService addressService, WebClient webClient
    ) {
        this.clientService = clientService;
        this.addressService = addressService;
        this.webClient = webClient;
    }

    @Override
    @Transactional
    public void updateClient(ClientAddressDto clientAddressDto) {
        clientService.update(Converters.converterClientUpdateDtoToClientDto(clientAddressDto));
    }

    @Override
    public void createClient(ClientAddressDto client) {
        ClientEntity clientEntity = dtoToClientEntity(client);
        AddressEntity addressEntity = dtoToAddressEntity(client);
        addressEntity.setClient(ClientServiceImpl.dtoToEntity(clientService.create(ClientServiceImpl.entityToDto(clientEntity)).get()));
        addressService.create(AddressServiceImpl.entityToDto(addressEntity));
        System.out.println(client);
    }

    @Override
    public void createAddress(ClientAddressDto client) {

        clientService.findByKey(client.getClientId()).ifPresent(clientEntity -> {

            AddressEntity addressEntity = dtoToAddressEntity(client);
            addressEntity.setClient(ClientServiceImpl.dtoToEntity(clientEntity));
            addressService.create(AddressServiceImpl.entityToDto(addressEntity));
        });

    }

    @Override
    public Stream<ClientAddressDto> findAll() {
        try {
            return webClient
                    .get()
                    .uri("/api/v1/client")
                    .retrieve()
                    .bodyToFlux(ClientEntity.class)
                    .collectList()
                    .block()
                    .stream()
                    .map(ClientServiceImpl::entityToDto)
                    .flatMap(this::clientDtoToClientAddressDto);
        }catch (Exception ee){
            throw new EAppException("Произошла ошибка подключения к web-сервису. Повторите попытку позже или обратитесь к администратору.");
        }
    }

    public Stream<ClientAddressDto> clientDtoToClientAddressDto(ClientDto clientDto) {
        if (addressService.findAddressByClientId(clientDto.getClientId()).count() == 0) {
            return Stream.of(Converters.converterClientUpdateDtoToClientDto(clientDto, null));
        } else {
            return addressService.findAddressByClientId(clientDto.getClientId()).map(address -> Converters.converterClientUpdateDtoToClientDto(clientDto, address));
        }
    }

    @Override
    public Optional<ClientDto> findByIdClient(Long id) {
        return clientService.findByKey(id);
    }

    @Override
    public Optional<AddressDto> findByMacAddress(String mac) {
        return addressService.findByKey(mac);
    }

    @Override
    public List<ClientAddressDto> findAllClientWithNameOrAddress(String clientType, String clientNameOrAddress) {
        List<ClientAddressDto> clients = findAll().toList();
        List<ClientAddressDto> clientAddress = filterClientAddress(clients, clientNameOrAddress);
        List<ClientAddressDto> clientName = filterClientName(clients, clientNameOrAddress);
        if (!clientAddress.isEmpty() && clientAddress.size() != clients.size()) {
            clients = filterClientType(clientType, clientAddress);
        } else if (!clientName.isEmpty() && clientName.size() != clients.size()) {
            clients = filterClientType(clientType, clientName);
        } else {
            clients = filterClientType(clientType, clients);
        }
        return clients;
    }

    @Override
    public List<ClientAddressDto> filterClientName(List<ClientAddressDto> clients, String data) {
        if (data != null && !data.isEmpty()) {
            clients = clients.stream().filter(client -> client.getClientName().toLowerCase()
                    .contains(data.toLowerCase())).toList();
        }
        return clients;
    }

    @Override
    public List<ClientAddressDto> filterClientAddress(List<ClientAddressDto> clients, String data) {
        if (data != null && !data.isEmpty()) {
            clients = clients.stream().filter(client -> client.getAddress().toLowerCase()
                    .contains(data.toLowerCase())).toList();
        }
        return clients;
    }

    @Override
    public List<ClientAddressDto> filterClientType(String type, List<ClientAddressDto> list) {
        List<ClientAddressDto> newList = new ArrayList<>();
        if (type != null && !type.equals("") && !type.equals("--> Выберите тип <--")) {
            List<ClientAddressDto> finalNewList = newList;
            list.forEach(client -> {
                if (client.getClientType().equals(type)) {
                    finalNewList.add(client);
                }
            });
        } else newList = list;
        return newList;
    }

    public static ClientEntity dtoToClientEntity(ClientAddressDto dto) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientName(dto.getClientName());
        clientEntity.setClientType(dto.getClientType());
        clientEntity.setDatereg(dto.getDatereg());
        return clientEntity;
    }

    public static AddressEntity dtoToAddressEntity(ClientAddressDto dto) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setIp(dto.getIp());
        addressEntity.setMac(dto.getMac());
        addressEntity.setModel(dto.getModel());
        addressEntity.setAddress(dto.getAddress());
        return addressEntity;
    }
}
