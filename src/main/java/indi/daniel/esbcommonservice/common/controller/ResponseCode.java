package indi.daniel.esbcommonservice.common.controller;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),

    REQUEST_BODY_VALID_FAIL("1001", "輸入參數錯誤"),
    REQUEST_BODY_PARSE_FAIL("1002", "輸入參數無法解析"),
    REQUEST_METHOD_NOT_SUPPORTED("1003", "HTTP METHOD 不支援"),
    MEDIA_TYPE_NOT_SUPPORTED("1004", "HTTP CONTENT TYPE 不支援"),
    MISSING_REQUEST_PARAMETER("1005", "缺少輸入參數"),
    NOT_FOUND_ERROR("1006", "找不到資源"),
    PARAM_CONVERT_FAIL("1007", "參數轉換錯誤"),
    NO_RESOURCE_FOUND("1008", "找不到對應資源(URI)"),
    REQUEST_PARAM_VALID_FAIL("1009", "輸入參數檢核失敗"),
    INVALID_JWT_TOKEN_EXCEPTION("1010", "JWT TOKEN驗證失敗"),

    AUTH_TAX_ID_FAIL("2001", "認證統編失敗"),
    SEND_MAIL_FAIL("2002", "發送郵件失敗"),
    DATA_ACCESS_FAIL("2003", "資料庫存取失敗"),
    BACKEND_INVALID_RESPONSE("2004", "交易結果不符預期"),

    INVALID_CLIENT_ID_OR_SECRET("3001", "無效的client id或client secret"),
    INVALID_AUTHORIZED_CLIENT_SCOPE("3002", "無效的client授權範圍"),
    SCOPE_WITH_NO_API("3003", "scope內未定義API"),

    ERROR("9999", "系統錯誤，請聯繫系統人員");

    private final String code;

    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
