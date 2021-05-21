package com.crumbs.userservice.services;

import com.crumbs.userservice.exceptions.SendEmailFailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

import static com.crumbs.userservice.utility.Constants.DEV_APP_URL;
import static com.crumbs.userservice.utility.Constants.PRODUCTION_APP_URL;

@Service
public class EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;

    public EmailService(TemplateEngine templateEngine, JavaMailSender emailSender) {
        this.templateEngine = templateEngine;
        this.emailSender = emailSender;
    }

    public void sendConfirmRegistrationEmail(String to, String subject, String token, String userName) {

        Context context = new Context();
        String htmlContent = templateEngine.process("confirm_registration_email.html", context);

        htmlContent = htmlContent.replace("registration-url", DEV_APP_URL + "/login?token=" + token);
        htmlContent = htmlContent.replace("receiver-name", userName);

        sendEmail(to, subject, htmlContent);
    }

    public void sendEmail(String to, String subject, String htmlContent) {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom("noreply@crumbsapp.com");
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new SendEmailFailException("Email could not be sent");
        }
    }
}
