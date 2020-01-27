package org.itstep.msk.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Controller
public class MailController {
    @Autowired
    private JavaMailSender mailer;

    @Autowired
    private TemplateEngine engine;

    @GetMapping("/send")
    public String mail(
            @RequestParam(name = "plain", defaultValue = "") String plain
    ) throws MessagingException {
        MimeMessage mail = mailer.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);

        Context context = new Context();
        context.setVariable("title", "Заголовок!");
        context.setVariable("body", "Содержимое письма");
        String html = engine.process("mail", context);

        helper.setFrom("xvorostenok@mail.ru");
        helper.setTo(" A_katkoff@mail.ru");
        helper.setSubject("IT WORKS");
        helper.setText(html, plain.length() == 0);

        FileSystemResource attachment = new FileSystemResource(
                new File("C:\\Users\\Acceil\\Desktop\\WORKS.txt")
        );

        helper.addAttachment("SANYA.txt", attachment);

        mailer.send(mail);

        return "mail";
    }
}
