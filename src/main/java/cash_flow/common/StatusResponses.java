package cash_flow.common;

import lombok.Getter;

@Getter
public enum StatusResponses {

    SUCCESS("Success"),
    ALREADY_EXISTS("Already Exists"),
    FAILURE("Failure"),
    NOT_FOUND("Not Found"),
    UNAUTHORIZED("Unauthorized"),
    FORBIDDEN("Forbidden"),
    BAD_REQUEST("Bad Request"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private final String message;

    StatusResponses(String message) {
        this.message = message;
    }

}
