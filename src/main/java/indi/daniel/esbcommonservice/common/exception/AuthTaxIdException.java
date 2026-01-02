package indi.daniel.esbcommonservice.common.exception;

public class AuthTaxIdException extends RuntimeException {

    public AuthTaxIdException(){
    }

    public AuthTaxIdException(String message) {
        super(message);
    }

    public AuthTaxIdException(String message, Throwable cause) {
        super(message, cause);
    }
}