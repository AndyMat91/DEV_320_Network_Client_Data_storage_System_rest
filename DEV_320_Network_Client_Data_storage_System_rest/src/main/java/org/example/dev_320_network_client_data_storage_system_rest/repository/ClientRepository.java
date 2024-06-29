package org.example.dev_320_network_client_data_storage_system_rest.repository;

import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.ClientEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface ClientRepository {
    Stream<ClientEntity> findAll();

    Optional<ClientEntity> findClientById(Long id);

    void remove(Long id);

    Optional<ClientEntity> update(ClientEntity client);

    Optional<ClientEntity> create(ClientEntity client);
}

