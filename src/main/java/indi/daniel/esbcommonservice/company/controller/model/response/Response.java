package indi.daniel.esbcommonservice.company.controller.model.response;

import indi.daniel.esbcommonservice.common.controller.model.RestServiceResponse;
import indi.daniel.esbcommonservice.company.repository.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Response extends RestServiceResponse {

    @Schema(description = "Employee回傳結果")
    private Employee result;
}
