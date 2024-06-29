package org.example.dev_320_network_client_data_storage_system_rest.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "address")
public class AddressEntity {
    @Id
    @Column(name = "mac", nullable = false, length = 20)
    private String mac;

    @Column(name = "ip", length = 25)
    private String ip;

    @Column(name = "model", length = 200)
    private String model;

    @Column(name = "address", length = 200)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cl_id")
    private ClientEntity client;

}