package indi.daniel.esbcommonservice.common.client.http;

import indi.daniel.esbcommonservice.common.exception.HttpClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class WebHttpClient {

    @Autowired
    private WebClient webClient;

    public String postCall(String url, Map<String, String> headers, String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Optional.ofNullable(headers).ifPresent(h -> h.forEach(httpHeaders::add));

        return webClient.post()
                .uri(url)
                .headers(h -> h.addAll(httpHeaders))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).flatMap(
                                responseBody -> Mono.error(new HttpClientException(
                                        String.format(
                                                "API call failed, responseCode: %s, responseBody: %s", response.statusCode(), responseBody
                                        )
                                )
                        )
                ))
                .bodyToMono(String.class)
                .onErrorMap(e -> {
                    if (e instanceof java.net.ConnectException) {
                        return new HttpClientException("Connection error, url: " + url + ", error: " + e.getMessage());
                    } else if (e instanceof TimeoutException) {
                        return new HttpClientException("Socket timeout, url: " + url + ", error: " + e.getMessage());
                    }
                    return new HttpClientException("Network timeout, url: " + url + ", error: " + e.getMessage());
                })
                .block();
    }
}
