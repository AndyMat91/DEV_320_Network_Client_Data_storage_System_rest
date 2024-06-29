package org.example.dev_310_1_network_client_data_storage_system.service.impl;

import org.example.dev_310_1_network_client_data_storage_system.common.dto.ClientDto;
import org.example.dev_310_1_network_client_data_storage_system.common.entity.ClientEntity;
import org.example.dev_310_1_network_client_data_storage_system.common.enums.CLIENT_TYPE;
import org.example.dev_310_1_network_client_data_storage_system.common.exceptions.EAppException;
import org.example.dev_310_1_network_client_data_storage_system.service.ClientService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ClientServiceImpl implements ClientService {
    private final WebClient webClient;

    public ClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public Optional<ClientDto> create(ClientDto clientDto) {
        return Optional.ofNullable(webClient
                        .post()
                        .uri("/api/v1/client")
                        .bodyValue(clientDto)
                        .retrieve()
                        .bodyToMono(ClientDto.class)
                        .block());
    }

    @Override
    public void remove(Long id) throws EAppException, UnknownError {
        try {
            webClient
                    .delete()
                    .uri("/api/v1/client/remove/{id}", id)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            if (e instanceof EAppException) {
                throw new EAppException(String.format("Объект с идентификатором '%d' не найден", id));
            } else throw new UnknownError("Неизвестная ошибка, обратитесь к администратору");
        }
    }

    @Override
    public void update(ClientDto clientDto) throws EAppException, UnknownError {
        try {
            Optional.ofNullable(webClient
                            .post()
                            .uri("/api/v1/client/update")
                            .bodyValue(clientDto)
                            .retrieve()
                            .bodyToMono(ClientDto.class)
                            .block());
        } catch (Exception e) {
            if (e instanceof EAppException) {
                throw new EAppException(String.format("Не удалось обновить ClientDto"));
            } else throw new UnknownError("Неизвестная ошибка, обратитесь к администратору");
        }
    }

    @Override
    public Stream<ClientDto> findAll() {
        try {
            return webClient
                    .get()
                    .uri("/api/v1/client")
                    .retrieve()
                    .bodyToFlux(ClientEntity.class)
                    .collectList()
                    .block()
                    .stream()
                    .map(ClientServiceImpl::entityToDto);
        }catch (Exception ee){
            throw new EAppException("Произошла ошибка подключения к web-сервису. Повторите попытку позже или обратитесь к администратору.");
        }
    }

    @Override
    public Optional<ClientDto> findByKey(Long id) {
        try {
            return webClient
                    .get()
                    .uri("/api/v1/client/{id}", id)
                    .retrieve()
                    .bodyToFlux(ClientEntity.class)
                    .collectList()
                    .map(list -> list.stream().map(ClientServiceImpl::entityToDto).findFirst())
                    .block();
        } catch (Exception ee) {
            throw new EAppException("Произошла ошибка подключения к web-сервису. Повторите попытку позже или обратитесь к администратору.");
        }
    }


    public static ClientDto entityToDto(ClientEntity entity) {
        return ClientDto.builder()
                .clientId(entity.getClientId())
                .clientName(entity.getClientName())
                .clientType(CLIENT_TYPE.getType(entity.getClientType()))
                .datereg(entity.getDatereg())
                .build();
    }

    public static ClientEntity dtoToEntity(ClientDto dto) {
        ClientEntity entity = new ClientEntity();
        entity.setClientId(dto.getClientId());
        entity.setClientName(dto.getClientName());
        entity.setClientType(dto.getClientType().getValue());
        entity.setDatereg(dto.getDatereg());
        return entity;
    }
}
