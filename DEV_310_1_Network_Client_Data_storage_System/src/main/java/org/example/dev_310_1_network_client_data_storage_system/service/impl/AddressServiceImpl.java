package org.example.dev_310_1_network_client_data_storage_system.service.impl;

import org.example.dev_310_1_network_client_data_storage_system.common.dto.AddressDto;
import org.example.dev_310_1_network_client_data_storage_system.common.entity.AddressEntity;
import org.example.dev_310_1_network_client_data_storage_system.common.exceptions.EAppException;
import org.example.dev_310_1_network_client_data_storage_system.service.AddressService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AddressServiceImpl implements AddressService {
    private final WebClient webClient;


    public AddressServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<AddressDto> create(AddressDto addressDto) {
        return Optional.ofNullable(webClient
                        .post()
                        .uri("/api/v1/address")
                        .bodyValue(addressDto)
                        .retrieve()
                        .bodyToMono(AddressDto.class)
                        .block());
    }

    @Override
    public void remove(String mac) throws EAppException, UnknownError {
        try {
            webClient
                    .delete()
                    .uri("/api/v1/address/remove/{mac}", mac)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            if (e instanceof EAppException) {
                throw new EAppException(String.format("Адрес с mac: '%d' не найден", mac));
            } else throw new UnknownError("Неизвестная ошибка, обратитесь к администратору");
        }
    }

    @Override
    public void update(AddressDto addressDto) throws EAppException, UnknownError {
        try {
            Optional.ofNullable(webClient
                            .post()
                            .uri("/api/v1/address/update")
                            .bodyValue(dtoToEntity(addressDto))
                            .retrieve()
                            .bodyToMono(AddressEntity.class)
                            .block())
                    .map(AddressServiceImpl::entityToDto);
        } catch (Exception e) {
            if (e instanceof EAppException) {
                throw new EAppException(String.format("Не удалось обновить AddressDto"));
            } else throw new UnknownError("Неизвестная ошибка, обратитесь к администратору");
        }
    }

    @Override
    public Stream<AddressDto> findAll() {
        try {
            return webClient
                    .get()
                    .uri("/api/v1/address")
                    .retrieve()
                    .bodyToFlux(AddressEntity.class)
                    .collectList()
                    .block()
                    .stream()
                    .map(AddressServiceImpl::entityToDto);
        } catch (Exception ee) {
            throw new EAppException("Произошла ошибка подключения к web-сервису. Повторите попытку позже или обратитесь к администратору.");
        }
    }

    @Override
    public Optional<AddressDto> findByKey(String mac) {
        try {
            return webClient
                    .get()
                    .uri("/api/v1/address/mac/{mac}", mac)
                    .retrieve()
                    .bodyToFlux(AddressEntity.class)
                    .collectList()
                    .map(list -> list.stream().map(AddressServiceImpl::entityToDto).findFirst())
                    .block();
        } catch (Exception ee) {
            throw new EAppException("Произошла ошибка подключения к web-сервису. Повторите попытку позже или обратитесь к администратору.");
        }
    }

    @Override
    public Stream<AddressDto> findAddressByClientId(Long id) {
        try {
            return webClient
                    .get()
                    .uri("/api/v1/address/id/{id}", id)
                    .retrieve()
                    .bodyToFlux(AddressEntity.class)
                    .collectList()
                    .map(list -> list.stream().map(AddressServiceImpl::entityToDto))
                    .block();
        } catch (Exception ee) {
            throw new EAppException("Произошла ошибка подключения к web-сервису. Повторите попытку позже или обратитесь к администратору.");
        }
    }

    public static AddressDto entityToDto(AddressEntity entity) {
        return AddressDto.builder()
                .ip(entity.getIp())
                .address(entity.getAddress())
                .mac(entity.getMac())
                .model(entity.getModel())
                .address(entity.getAddress())
                .clientDto(ClientServiceImpl.entityToDto(entity.getClient()))
                .build();
    }

    public static AddressEntity dtoToEntity(AddressDto dto) {
        AddressEntity entity = new AddressEntity();
        entity.setIp(dto.getIp());
        entity.setAddress(dto.getAddress());
        entity.setMac(dto.getMac());
        entity.setModel(dto.getModel());
        entity.setAddress(dto.getAddress());
        entity.setClient(ClientServiceImpl.dtoToEntity(dto.getClientDto()));
        return entity;
    }
}
