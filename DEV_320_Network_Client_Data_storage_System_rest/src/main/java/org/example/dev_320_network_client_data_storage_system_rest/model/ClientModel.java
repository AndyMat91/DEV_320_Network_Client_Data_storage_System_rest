package org.example.dev_320_network_client_data_storage_system_rest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.example.dev_320_network_client_data_storage_system_rest.enums.CLIENT_TYPE;
import org.example.dev_320_network_client_data_storage_system_rest.repository.entity.AddressEntity;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class ClientModel {
    private Long clientId;

    @NotBlank(message = "Имя клиента не может быть пустым!")
    @Size(max = 100, message = "Имя клиента не может содержать более 100 символов!")
    @Pattern(regexp = "^$|[а-яА-Я-,. ]+", message = "Недопустимые символы в имени клиента!")
    private String clientName;

    private CLIENT_TYPE clientType;

    private Instant datereg;

    private Set<AddressEntity> addresses;


    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + clientId +
                ", name='" + clientName + '\'' +
                ", type=" + clientType +
                ", added=" + datereg +
                ", addresses=" + addresses +
                '}';
    }
}
