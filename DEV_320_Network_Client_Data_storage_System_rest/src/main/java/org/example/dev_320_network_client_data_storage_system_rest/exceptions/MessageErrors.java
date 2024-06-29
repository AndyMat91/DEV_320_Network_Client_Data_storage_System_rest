package org.example.dev_320_network_client_data_storage_system_rest.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageErrors {

    private List<Violation> violations;

    @Data
    @AllArgsConstructor
    public static class Violation{

        public String fieldName;
        public String message;

    }
}
