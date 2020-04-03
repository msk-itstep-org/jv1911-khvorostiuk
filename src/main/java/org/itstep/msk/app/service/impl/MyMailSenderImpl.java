package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.service.MyMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class MyMailSenderImpl implements MyMailSender {
    private TemplateEngine templateEngine;
    private JavaMailSender mailSender;

    @Autowired
    public MyMailSenderImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void send(String emailTo, String subject, String message, Map<String, Object> parameters) throws Exception {
        Context context = new Context();
        for (String key : parameters.keySet()) {
            context.setVariable(
                    key,
                    parameters.get(key)
            );
        }
        String html = templateEngine.process(message, context);
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "UTF-8");

        helper.setFrom(username);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(mailMessage);
    }
}
