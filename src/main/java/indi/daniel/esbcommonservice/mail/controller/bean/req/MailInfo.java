package indi.daniel.esbcommonservice.mail.controller.bean.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "郵件資訊物件")
public class MailInfo {

    @NotBlank
    @Schema(description = "寄件人", example = "sender@example.com")
    private String from;

    @NotBlank
    @Schema(description = "收件人列表", example = "recipient1@example.com")
    private String to;

    @NotBlank
    @Schema(description = "郵件主旨", example = "測試郵件主旨")
    private String subject;
}
