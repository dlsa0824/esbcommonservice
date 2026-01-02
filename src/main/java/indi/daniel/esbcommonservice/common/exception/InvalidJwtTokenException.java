package indi.daniel.esbcommonservice.common.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException() {
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
