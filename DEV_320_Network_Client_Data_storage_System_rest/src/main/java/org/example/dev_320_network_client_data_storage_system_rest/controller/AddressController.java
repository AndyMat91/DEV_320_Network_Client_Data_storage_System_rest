package org.example.dev_320_network_client_data_storage_system_rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dev_320_network_client_data_storage_system_rest.dto.AddressDto;
import org.example.dev_320_network_client_data_storage_system_rest.model.AddressModel;
import org.example.dev_320_network_client_data_storage_system_rest.repository.AddressRepository;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.AddressEntity;
import org.example.dev_320_network_client_data_storage_system_rest.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;
    private final AddressService addressService;


    @GetMapping(value = "/id/{id}", produces = "application/json")
    public ResponseEntity<?> getAddressByIdClient(@PathVariable("id") Long id){
        log.info("Поступил запрос на предоставление адреса по id клиента: " + id);
        Stream<AddressEntity> address = addressRepository.findAddressByClientId(id);
        log.info("Адрес предоставлен");
        if(address!=null) return ResponseEntity.ok(address);
        else return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/mac/{mac}", produces = "application/json")
    public ResponseEntity<?> getAddressByMac(@PathVariable("mac") String mac){
        log.info("Поступил запрос на предоставление адреса по mac: " + mac);
        Optional<AddressEntity> address = addressRepository.findAddressByMac(mac);
        if(address.isPresent()) return ResponseEntity.ok(address);
        else return ResponseEntity.ok().build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> findAll(){
        log.info("Поступил запрос на поиск всех адресов");
        List<AddressEntity> adresses = addressRepository.findAll().toList();
        return ResponseEntity.ok(adresses);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<AddressModel> createAddress(@RequestBody AddressModel address) {
        log.info("Поступил запрос на создание адреса: " + address);
        addressService.create(modelToDto(address));
        log.info("Новый адрес создан");
        return ResponseEntity.ok(address);
    }

    @DeleteMapping(value = "/{mac}")
    public ResponseEntity<?> removeAddress(@PathVariable("mac") String mac) {
        log.info("Поступил запрос на удаление адреса по mac: " + mac);
        addressRepository.remove(mac);
        log.info("Адрес с mac: " + mac + " удален");
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<AddressModel> updateAddress(@RequestBody AddressModel address) {
        log.info("Поступил запрос на обновление адреса: " + address);
        addressService.update(modelToDto(address));
        log.info("Адрес обновлен");
        return ResponseEntity.ok(address);
    }

    private AddressDto modelToDto(AddressModel model){
        return AddressDto.builder()
                .mac(model.getMac())
                .ip(model.getIp())
                .address(model.getAddress())
                .model(model.getModel())
                .clientDto(model.getClientDto())
                .build();
    }
}