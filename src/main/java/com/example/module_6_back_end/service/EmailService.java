package com.example.module_6_back_end.service;

import com.example.module_6_back_end.model.Contract;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final PdfService pdfService;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, PdfService pdfService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.pdfService = pdfService;
    }

    public void sendMail(Contract contract) throws MessagingException {
        byte[] pdfData = pdfService.getPdf(contract.getId());
        Context context = new Context();
        context.setVariable("contract", contract);
        String html = templateEngine.process("email", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            messageHelper.setTo(contract.getCustomer().getEmail());
            messageHelper.setSubject("Hợp Đồng Thuê Mặt Bằng");
            messageHelper.setText(html, true);
            messageHelper.setFrom("support@yourcompany.com");
            messageHelper.addAttachment("contract.pdf", new ByteArrayResource(pdfData));
            mailSender.send(message);
    }
}