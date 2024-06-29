package org.example.dev_320_network_client_data_storage_system_rest.repository;

import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.AddressEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface AddressRepository {
    Stream<AddressEntity> findAll();

    Optional<AddressEntity> findAddressByMac(String mac);

    Stream<AddressEntity> findAddressByClientId(Long id);
    void remove(String mac);

    Optional<AddressEntity> update(AddressEntity address);

    Optional<AddressEntity> create(AddressEntity address);
}
