package indi.daniel.esbcommonservice.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class Base64Utils {

    /**
     * 使用 Base64 URL 安全模式編碼字串。
     *
     * @param text 要編碼的字串。
     * @return 編碼後的字串。
     */
    public String encodeUrlSafe(String text) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 使用 Base64 URL 安全模式解碼字串。
     *
     * @param encodedText 要解碼的 Base64 URL 安全編碼字串。
     * @return 解碼後的字串。
     */
    public String decodeUrlSafe(String encodedText) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
