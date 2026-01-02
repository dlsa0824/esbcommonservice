package indi.daniel.esbcommonservice.auth.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("0000");

    final String code;

    ResponseCode(String code) {
        this.code = code;
    }
}
