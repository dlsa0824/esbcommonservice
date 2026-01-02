package indi.daniel.esbcommonservice.common.exception;

import java.io.IOException;

public class HttpClientException extends IOException {

    public HttpClientException() {
    }

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
