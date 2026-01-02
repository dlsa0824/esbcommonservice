package indi.daniel.esbcommonservice.iam.exception;

public class InvalidClientScopeException extends RuntimeException{

    public InvalidClientScopeException() {
    }

    public InvalidClientScopeException(String message) {
        super(message);
    }
}
