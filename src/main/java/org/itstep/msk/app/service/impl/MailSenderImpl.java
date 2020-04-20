package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class MailSenderImpl implements MailSender {
    private TemplateEngine templateEngine;
    private JavaMailSender mailSender;
    private String username;

    public MailSenderImpl(
            @Autowired TemplateEngine templateEngine,
            @Autowired JavaMailSender mailSender,
            @Value("${spring.mail.username}") String username
    ) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
        this.username = username;
    }

    @Override
    public void send(String emailTo, String subject, String text) throws MessagingException {
        final MimeMessage message = createMessage(emailTo, subject, text);

        mailSender.send(message);
    }

    @Override
    public void send(
            String emailTo,
            String subject,
            String view,
            Map<String, Object> parameters
    ) throws MessagingException {
        String text = renderView(view, parameters);
        MimeMessage message = createMessage(emailTo, subject, text, true);

        mailSender.send(message);
    }

    private MimeMessage createMessage(String emailTo, String subject, String text) throws MessagingException {
        return createMessage(emailTo, subject, text, false);
    }

    private MimeMessage createMessage(
            String emailTo,
            String subject,
            String text,
            boolean isHtml
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setFrom(username);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(text, isHtml);

        return message;
    }

    private String renderView(String view, Map<String, Object> parameters) {
        Context context = new Context();
        context.setVariables(parameters);

        return templateEngine.process(view, context);
    }
}
