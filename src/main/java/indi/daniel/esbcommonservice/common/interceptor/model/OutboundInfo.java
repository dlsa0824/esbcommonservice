package indi.daniel.esbcommonservice.common.interceptor.model;

import lombok.Data;

import java.util.Map;

@Data
public class OutboundInfo {

    private String method;

    private String url;

    private Map<String, String> headers;

    private Object requestBody;

    private Object responseBody;

    private int status;

    private long costTime;
}