package net.thumbtack.hospital.util.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("hospital.errors")
public class ErrorMessageFactory {
    @Value("#{${hospital.errors.validations-error-codes-to-error-messages}}")
    private final Map<String, String> validationsErrorCodesToErrorMessages = new HashMap<>();

    public String getValidationMessageByCode(String errorCode) {
        return validationsErrorCodesToErrorMessages.get(errorCode);
    }
}