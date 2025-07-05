package cash_flow.common;

import lombok.Getter;

@Getter
public enum StatusResponses {

    SUCCESS("Success"),
    ALREADY_EXISTS("already Exists"),
    FAILURE("failed to complete the operation"),
    NOT_FOUND("not Found"),
    UNAUTHORIZED("Unauthorized"),
    FORBIDDEN("Forbidden"),
    BAD_REQUEST("Bad Request"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private final String message;

    StatusResponses(String message) {
        this.message = message;
    }

}
