package org.example.dev_320_network_client_data_storage_system_rest.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.dev_320_network_client_data_storage_system_rest.repository.AddressRepository;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class AddressRepositoryImpl implements AddressRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Stream<AddressEntity> findAll() {
        return em.createNativeQuery("select * from address", AddressEntity.class).getResultList().stream();
    }

    @Override
    public Optional<AddressEntity> findAddressByMac(String mac) {
        return Optional.of(mac).map(obj -> em.find(AddressEntity.class, obj));
    }

    @Override
    public Stream<AddressEntity> findAddressByClientId(Long id) {
        String query = "select * from address where cl_id=" + id;
        return em.createNativeQuery(query, AddressEntity.class).getResultList().stream();
    }

    @Override
    @Transactional
    public void remove(String mac) throws UnknownError {
        findAddressByMac(mac).ifPresent(address -> em.remove(address));
    }

    @Override
    @Transactional
    public Optional<AddressEntity> update(AddressEntity address) {
        findAddressByMac(address.getMac()).ifPresentOrElse(
                entity -> {
                    entity.setIp(address.getIp());
                    entity.setModel(address.getModel());
                    entity.setAddress(address.getAddress());
                    em.merge(entity);
                    em.flush();
                },
                () -> {
                }
        );
        return Optional.of(address);

    }

    @Override
    @Transactional
    public Optional<AddressEntity> create(AddressEntity address) {
        em.persist(address);
        return Optional.of(address);
    }
}