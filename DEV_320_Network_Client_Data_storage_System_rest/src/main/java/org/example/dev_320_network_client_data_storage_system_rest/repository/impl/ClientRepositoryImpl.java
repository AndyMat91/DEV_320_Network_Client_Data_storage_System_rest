package org.example.dev_320_network_client_data_storage_system_rest.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.dev_320_network_client_data_storage_system_rest.repository.ClientRepository;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.ClientEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Stream<ClientEntity> findAll() {
        return em.createNativeQuery("select * from clients", ClientEntity.class).getResultList().stream();
    }

    @Override
    public Optional<ClientEntity> findClientById(Long id) {
        return Optional.of(id).map(obj -> em.find(ClientEntity.class, obj));
    }

    @Override
    @Transactional
    public void remove(Long id) {
        findClientById(id).ifPresent(client -> em.remove(client));
    }

    @Override
    @Transactional
    public Optional<ClientEntity> update(ClientEntity client) {
        findClientById(client.getClientId()).ifPresentOrElse(
                entity -> {
                    entity.setClientName(client.getClientName());
                    entity.setClientType(client.getClientType());
                    entity.setDatereg(client.getDatereg());
                    em.merge(entity);
                    em.flush();
                },
                () -> {
                }
        );
        return Optional.of(client);
    }


    @Override
    @Transactional
    public Optional<ClientEntity> create(ClientEntity client) {
        em.persist(client);
        return Optional.of(client);
    }
}