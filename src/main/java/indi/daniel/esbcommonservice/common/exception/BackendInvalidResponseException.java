package indi.daniel.esbcommonservice.common.exception;

public class BackendInvalidResponseException extends RuntimeException {

    public BackendInvalidResponseException() {
    }

    public BackendInvalidResponseException(String message) {
        super(message);
    }

}
