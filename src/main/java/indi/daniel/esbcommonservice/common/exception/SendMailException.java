package indi.daniel.esbcommonservice.common.exception;

import java.io.IOException;

public class SendMailException extends IOException {

    public SendMailException() {
    }

    public SendMailException(String message) {
        super(message);
    }

    public SendMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendMailException(Throwable cause) {
        super(cause);
    }
}
