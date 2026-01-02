package indi.daniel.esbcommonservice.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailConst {

    public static String mailTemplate;

    @Value("${esb.mail.template}")
    public void setMailTemplate(String mailTemplate) {
        MailConst.mailTemplate = mailTemplate;
    }
}
