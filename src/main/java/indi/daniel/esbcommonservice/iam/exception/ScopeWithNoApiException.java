package indi.daniel.esbcommonservice.iam.exception;


public class ScopeWithNoApiException extends RuntimeException{

    public ScopeWithNoApiException() {
    }

    public ScopeWithNoApiException(String message) {
        super(message);
    }

}
