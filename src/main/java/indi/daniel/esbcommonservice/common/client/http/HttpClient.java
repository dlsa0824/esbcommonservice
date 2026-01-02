package indi.daniel.esbcommonservice.common.client.http;

import indi.daniel.esbcommonservice.common.exception.HttpClientException;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Optional;

@Component
public class HttpClient {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    private OkHttpClient httpClient;

    public String postCall(String url, Map<String, String> headers, String body) throws IOException {

        Headers.Builder headersBuilder = new Headers.Builder();
        // if (headers != null) {
        //     headers.forEach(headersBuilder::add);
        // }
        Optional.ofNullable(headers).ifPresent(h -> h.forEach(headersBuilder::add));

        RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(url)
                .headers(headersBuilder.build())
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
//            if (!response.isSuccessful()) {
//                String responseCode = Integer.toString(response.code());
//                String responseMessage = response.message();
//                throw new HttpClientException(String.format(
//                        "API call failed, responseCode: %s, responseMessage: %s, responseBody: %s",
//                        responseCode, responseMessage, responseBody
//                ));
//            }
            return responseBody;
        } catch (ConnectException e) {
            // 處理連線失敗的例外，例如目標主機拒絕連線或無法到達
            throw new HttpClientException("Connection error, url: " + url + ", error: " +  e.getMessage());
        } catch (SocketTimeoutException e) {
            // 處理連線逾時的例外，例如讀取或寫入逾時
            throw new IOException("Socket timeout, url: " + url + ", error: " +  e.getMessage());
        } catch (IOException e) {
            // 處理其他例外
            throw new HttpClientException("Network timeout, url: " + url + ", error: " +  e.getMessage());
        }
    }
}
