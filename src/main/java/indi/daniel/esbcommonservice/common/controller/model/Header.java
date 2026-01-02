package indi.daniel.esbcommonservice.common.controller.model;

import indi.daniel.esbcommonservice.common.CommonConst;
import indi.daniel.esbcommonservice.common.utils.HeaderUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "請求標頭物件")
public class Header {

    @NotBlank
    @Schema(description = "訊息編號", example = "msg_20250831214500_888")
    private String msgNo;

    @NotBlank
    @Schema(description = "交易時間", example = "20250831103000")
    private String txnTime;

    @NotBlank
    @Schema(description = "發送方代碼", example = "UP0000")
    private String senderCode;

    @NotBlank
    @Schema(description = "接收方代碼", example = "UP9999")
    private String receiverCode;

    @Schema(description = "操作代碼", example = "/api/service")
    private String operateCode;

    @Schema(description = "授權碼", example = "esb00001")
    private String authorizeCode;

    public Header(HeaderUtils headerUtils) {
        String txnTime = headerUtils.getTxnTime();
        this.msgNo = String.format("msg_%s_%s", txnTime, headerUtils.getRandomNumber());
        this.txnTime = txnTime;
        this.senderCode = CommonConst.SENDER_CODE;
    }
}
