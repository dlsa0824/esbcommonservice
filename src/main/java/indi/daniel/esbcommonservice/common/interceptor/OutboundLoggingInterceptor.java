package indi.daniel.esbcommonservice.common.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.daniel.esbcommonservice.common.interceptor.model.OutboundInfo;
import jakarta.annotation.PostConstruct;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(value = "esb.common.log.outbound.enabled", havingValue = "true", matchIfMissing = false)
public class OutboundLoggingInterceptor implements Interceptor {

    @Value("${esb.common.log.outbound.logger}")
    private String outboundLoggerName;

    private Logger outboundLogger;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        outboundLogger = LogManager.getLogger(outboundLoggerName);
        System.out.println("Started outbound interceptor");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.currentTimeMillis();
        Response response = null;

        try {
            response = chain.proceed(request);
        } finally {
            long endTime = System.currentTimeMillis();

            OutboundInfo outboundInfo = new OutboundInfo();
            outboundInfo.setUrl(request.url().toString());
            outboundInfo.setMethod(request.method());

            Map<String, String> headers = new HashMap<>();
            request.headers().forEach(pair -> headers.put(pair.getFirst(), pair.getSecond()));
            outboundInfo.setHeaders(headers);

            outboundInfo.setRequestBody(getRequestBody(request));
            outboundInfo.setResponseBody(getResponseBody(response));
            outboundInfo.setStatus(response.code());
            outboundInfo.setCostTime(endTime - startTime);

            outboundLogger.info(objectMapper.writeValueAsString(outboundInfo));
        }
        return response;
    }

    private Object getRequestBody(Request request) {
        String requestBodyString;
        try {
            if (request.body() != null) {
                Request copy = request.newBuilder().build();
                Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                requestBodyString = buffer.readString(StandardCharsets.UTF_8);
            } else {
                requestBodyString = "";
            }
        } catch (Exception e) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("text", "Error reading request body");
            return dataMap;
        }

        try {
            return objectMapper.readValue(requestBodyString, JsonNode.class);
        } catch (Exception e) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("text", requestBodyString);
            return dataMap;
        }
    }

    private Object getResponseBody(Response response) {
        String responseBodyString;
        try {
            responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        } catch (Exception e) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("text", "Error reading response body");
            return dataMap;
        }

        try {
            return objectMapper.readValue(responseBodyString, JsonNode.class);
        } catch (Exception e) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("text", responseBodyString);
            return dataMap;
        }
    }
}
