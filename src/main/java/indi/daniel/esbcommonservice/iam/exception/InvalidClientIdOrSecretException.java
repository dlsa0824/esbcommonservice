package indi.daniel.esbcommonservice.iam.exception;

public class InvalidClientIdOrSecretException extends RuntimeException{

    public InvalidClientIdOrSecretException() {
    }

    public InvalidClientIdOrSecretException(String message) {
        super(message);
    }

}
