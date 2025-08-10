package cash_flow.service;

import cash_flow.dto.incoming.GroupMemberCreationCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class InUIValidationService {

    private final Map<String, String> validationErrors;

    public InUIValidationService() {
        this.validationErrors = new HashMap<>();
    }

    public void addValidationError(String key, String errorMessage) {
        if (key == null || errorMessage == null) {
            log.error("Key or error message cannot be null");
            return;
        }
        validationErrors.put(key, errorMessage);
    }

    public String getValidationError(String key) {
        return validationErrors.get(key);
    }

    public String deleteValidationError(String key) {
        return validationErrors.remove(key);
    }

    public Optional<String> validateName(String name) {
        if (name == null || name.isBlank()) {
            String error = "Name validation failed: Name is blank or null";
            log.error(error);
            return Optional.of(error);
        }
        if (!name.matches("[a-zA-ZáéíóöőúüűÁÉÍÓÖŐÚÜŰ]+")) {
            String error = "Name validation failed: Name must contain only letters";
            log.error(error);
            return Optional.of(error);
        }
        if (name.length() < 2) {
            String error = "Name validation failed: Name is too short";
            log.error(error);
            return Optional.of(error);
        }
        if (!Character.isUpperCase(name.charAt(0))) {
            String error = "Name validation failed: Name must start with an uppercase letter";
            log.error(error);
            return Optional.of(error);
        }
        return Optional.empty();
    }

    public Optional<String> validateEmail(String email) {
        if (email == null || email.isBlank()) {
            String error = "Email validation failed: Email is blank";
            log.error(error);
            return Optional.of(error);
        }
        if (!email.contains("@") || !email.contains(".")) {
            String error = "Email validation failed: Email must contain '@' and '.'";
            log.error(error);
            return Optional.of(error);
        }
        if (email.length() < 5) {
            String error = "Email validation failed: Email is too short";
            return Optional.of(error);
        }
        return Optional.empty();
    }

}
