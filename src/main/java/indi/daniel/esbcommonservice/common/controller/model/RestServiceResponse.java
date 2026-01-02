package indi.daniel.esbcommonservice.common.controller.model;

import indi.daniel.esbcommonservice.common.controller.ResponseCode;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static indi.daniel.esbcommonservice.common.controller.ResponseCode.SUCCESS;

@Data
@Schema(description = "REST 服務回應物件")
public class RestServiceResponse {

    @Schema(description = "回應代碼")
    private String code;

    @Schema(description = "回應描述")
    private String description;

    @Schema(description = "錯誤訊息列表")
    private List<String> errorMessages;

    public RestServiceResponse() {
        this(SUCCESS);
    }

    public RestServiceResponse(ResponseCode httpStatus) {
        this.code = httpStatus.getCode();
        this.description = httpStatus.getDescription();
    }
}
