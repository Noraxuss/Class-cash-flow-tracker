package cash_flow.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ErrorUtilities {

    public String returnErrorMessage(String cause, StatusResponses statusResponse) {
        log.error("{} {}", cause, statusResponse.getMessage());
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append("Error: ").append(cause).append(" - ").append(statusResponse.getMessage());
        return errorBuilder.toString();
    }

}
