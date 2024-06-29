package org.example.dev_320_network_client_data_storage_system_rest.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dev_320_network_client_data_storage_system_rest.dto.ClientDto;
import org.example.dev_320_network_client_data_storage_system_rest.model.ClientModel;
import org.example.dev_320_network_client_data_storage_system_rest.repository.ClientRepository;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.AddressEntity;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.ClientEntity;
import org.example.dev_320_network_client_data_storage_system_rest.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getClient(@PathVariable("id") Long id){
        log.info("Поступил запрос на поиск клиента по id: " + id);
        ClientEntity client = clientRepository.findClientById(id).orElse(null);
        if(client!=null) return ResponseEntity.ok(client);
        else return ResponseEntity.ok().build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> findAll(){
        log.info("Поступил запрос на поиск всех клиентов");
        List<ClientEntity> clients = clientRepository.findAll().toList();
        return ResponseEntity.ok(clients);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ClientModel> createClient(@RequestBody ClientModel client){
        log.info("Поступил запрос на создание клиента: " + client);
       ClientDto clientDto = clientService.create(modelToDto(client));
        log.info("Новый клиент создан");
        return ResponseEntity.ok(dtoToModel(clientDto));
    }

    @DeleteMapping(value = "/remove/{id}", consumes = "application/json")
    public ResponseEntity<?> removeClient(@PathVariable("id") Long id) {
        log.info("Поступил запрос на удаление клиента по id: " + id);
        clientRepository.remove(id);
        log.info("Клиент c id: " + id + " удален");
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<ClientModel> updateClient(@RequestBody ClientModel client) {
        log.info("Поступил запрос на обновление клиента : " + client);
        clientService.update(modelToDto(client));
        log.info("Клиент обновлен");
        return ResponseEntity.ok(client);
    }

    private ClientDto modelToDto(ClientModel model){
        return ClientDto.builder()
                .clientId(model.getClientId())
                .clientName(model.getClientName())
                .clientType(model.getClientType())
                .datereg(model.getDatereg())
                .addresses(model.getAddresses())
                .build();
    }

    private ClientModel dtoToModel(ClientDto dto){
        return ClientModel.builder()
                .clientId(dto.getClientId())
                .clientName(dto.getClientName())
                .clientType(dto.getClientType())
                .datereg(dto.getDatereg())
                .addresses(dto.getAddresses())
                .build();
    }
}