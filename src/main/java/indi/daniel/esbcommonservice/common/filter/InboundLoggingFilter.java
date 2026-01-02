package indi.daniel.esbcommonservice.common.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.daniel.esbcommonservice.common.filter.model.InboundInfo;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "esb.common.log.inbound.enabled", havingValue = "true", matchIfMissing = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InboundLoggingFilter implements Filter {

    @Value("${esb.common.log.inbound.logger}")
    private String inboundLoggerName;

    private Logger inboundLogger;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        inboundLogger = LogManager.getLogger(inboundLoggerName);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String traceId = UUID.randomUUID().toString();
        ThreadContext.put("traceId", traceId);

        long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);

            InboundInfo inboundInfo = new InboundInfo();
            inboundInfo.setUrl(requestWrapper.getRequestURI());
            inboundInfo.setMethod(requestWrapper.getMethod());
            inboundInfo.setHeaders(getRequestHeaders(requestWrapper));
            inboundInfo.setRequest(getRequestBody(requestWrapper));
            inboundInfo.setResponse(getResponseBody(responseWrapper));
            inboundInfo.setStatus(responseWrapper.getStatus());

            long costTime = System.currentTimeMillis() - startTime;
            inboundInfo.setCostTime(costTime);

            inboundLogger.info(objectMapper.writeValueAsString(inboundInfo));
        } finally {
            responseWrapper.copyBodyToResponse();
            ThreadContext.remove("traceId");
        }
    }

    private Map<String, String> getRequestHeaders(ContentCachingRequestWrapper request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private Object getRequestBody(ContentCachingRequestWrapper request) {
        try {
            return objectMapper.readValue(request.getContentAsByteArray(), JsonNode.class);
        } catch (Exception e) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("text", new String(request.getContentAsByteArray()));
            return dataMap;
        }
    }

    private Object getResponseBody(ContentCachingResponseWrapper response) {
        try {
            return objectMapper.readValue(response.getContentAsByteArray(), JsonNode.class);
        } catch (Exception e) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("text", new String(response.getContentAsByteArray()));
            return dataMap;
        }
    }
}
