package indi.daniel.esbcommonservice.iam.controller.model.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssetCode {

    @NotBlank
    private String assetCode;

    @NotNull
    private Boolean validateExpiration;
}
