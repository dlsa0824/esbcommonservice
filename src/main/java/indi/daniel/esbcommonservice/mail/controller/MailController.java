package indi.daniel.esbcommonservice.mail.controller;

import indi.daniel.esbcommonservice.common.controller.model.RestServiceResponse;
import indi.daniel.esbcommonservice.mail.controller.bean.req.MailRequest;
import indi.daniel.esbcommonservice.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "郵件服務", description = "提供郵件寄送相關的 API")
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @Operation(summary = "寄送 HTML 郵件", description = "根據請求內容寄送 HTML 格式的郵件")
    @PostMapping(path = "/sendMail", consumes = "application/json", produces = "application/json")
    public RestServiceResponse sendMail(@RequestBody @Valid MailRequest mailRequest) throws Exception {
        String from = mailRequest.getRequestBody().getFrom();
        String to = mailRequest.getRequestBody().getTo();
        String subject = mailRequest.getRequestBody().getSubject();
        mailService.sendHtmlMail(from, to, subject);

        return new RestServiceResponse();
    }
}
