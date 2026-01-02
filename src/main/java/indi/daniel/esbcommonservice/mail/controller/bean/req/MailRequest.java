package indi.daniel.esbcommonservice.mail.controller.bean.req;

import indi.daniel.esbcommonservice.common.controller.model.Header;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "郵件請求物件")
public class MailRequest {

    @NotNull
    @Valid
    @Schema(description = "請求標頭")
    private Header header;

    @NotNull
    @Valid
    @Schema(description = "郵件內容")
    private MailInfo requestBody;
}
