package com.enao.team2.quanlynhanvien.service.mail;

import com.enao.team2.quanlynhanvien.dto.EmailDTO;
import com.enao.team2.quanlynhanvien.exception.BadRequestException;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public boolean sendMail(EmailDTO emailDTO) {
        try {
            //create mail message
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            message.setContent(emailDTO.getContent(), "text/html");

            helper.setTo(emailDTO.getRecipients());

            helper.setSubject(emailDTO.getSubject());

            //send mail
            mailSender.send(message);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
