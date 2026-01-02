package indi.daniel.esbcommonservice.mail.service;

import indi.daniel.esbcommonservice.common.exception.SendMailException;
import indi.daniel.esbcommonservice.mail.MailConst;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendHtmlMail(String from, String to, String subject) throws Exception{
        Context context = new Context();
        context.setVariable("emailAddress", to);
        context.setVariable("emailSubject", subject);

        String htmlContent = templateEngine.process(MailConst.mailTemplate, context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContent, true); // true 表示內容是 HTML

        try {
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new SendMailException(e);
        }
    }
}
