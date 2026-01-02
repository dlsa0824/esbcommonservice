package indi.daniel.esbcommonservice.common.filter.model;

import lombok.Data;

import java.util.Map;

@Data
public class InboundInfo {

    private String url;

    private String method;

    private Map<String, String> headers;

    private Object request;

    private Object response;

    private int status;

    private long costTime;
}
