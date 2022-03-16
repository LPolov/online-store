package com.online.store.onlineStoreenterprise.services.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
  private static final String ENCODING = "UTF-8";
  private static final String EMAIL_SUBJECT = "Confirm your email";
  private static final String FROM = "online.store@mail.com";
  private final JavaMailSender sender;

  @Override
  @Async
  public void send(String to, String message) {
    try{
      MimeMessage mimeMessage = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ENCODING);
      helper.setText(message, true);
      helper.setTo(to);
      helper.setSubject(EMAIL_SUBJECT);
      helper.setFrom(FROM);
      sender.send(mimeMessage);
    } catch (MessagingException e) {
      LOGGER.error("Failed to send email", e);
      throw new IllegalStateException("Failed to send email");
    }
  }
}
