package stay.with.me.common;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    SUCCESS(200, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Permission denied"),
    NOT_FOUND(404, "Data not found"),
    INTERNAL_ERROR(500, "An error occurred");


    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
