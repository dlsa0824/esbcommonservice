package indi.daniel.esbcommonservice.common.client;

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

@Component
public class HttpClient {

    @Autowired
    private OkHttpClient okHttpClient;

    public String postCall(String url, Map<String, String> headers, String requestBody) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(requestBody, mediaType);

        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            headers.forEach(headersBuilder::add);
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(headersBuilder.build())
                .post(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (ConnectException e) {
            // 處理連線失敗的例外，例如目標主機拒絕連線或無法到達
            System.err.println("Connection failed to " + url + ": " + e.getMessage());
            throw new IOException("Failed to connect to " + url, e);
        } catch (SocketTimeoutException e) {
            // 處理連線逾時的例外，例如讀取或寫入逾時
            System.err.println("Connection timeout for " + url + ": " + e.getMessage());
            throw new IOException("Timeout connecting to " + url, e);
        } catch (IOException e) {
            // 處理其他 IO 例外
            System.err.println("An I/O error occurred for " + url + ": " + e.getMessage());
            throw e;
        }
    }
}
