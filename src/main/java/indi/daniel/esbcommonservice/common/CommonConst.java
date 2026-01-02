package indi.daniel.esbcommonservice.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonConst {

    public static String SENDER_CODE = "UP9999";

    public static String serverPrefixUrl;

    @Value("${esb.common.server.prefix-url}")
    public void setServerPrefixUrl(String serverPrefixUrl) {
        CommonConst.serverPrefixUrl = serverPrefixUrl;
    }
}
