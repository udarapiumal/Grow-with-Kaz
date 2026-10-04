package com.example.demo.Service;


import com.example.demo.Model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String toAddress;
    private final String fromAddress;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.to}") String toAddress,
                        @Value("${app.mail.from}") String fromAddress) {
        this.mailSender = mailSender;
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
    }

    /**
     * Notifies the client of a new inquiry. Reply-To is set to the *visitor's*
     * email, so hitting Reply in Gmail/Outlook goes straight back to them —
     * the client never needs to copy an address out of the message body.
     *
     * Deliberately never throws: a saved contact message that fails to email
     * is recoverable (it's in the database); a contact form that 500s because
     * SMTP hiccuped is not. If email keeps failing, that shows up in the logs.
     */
    public void sendContactNotification(Contact message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toAddress);
        mail.setFrom(fromAddress);
        mail.setReplyTo(message.getEmail());
        mail.setSubject("New inquiry: " + message.getName() + " (" + message.getInquiryType() + ")");
        mail.setText("""
                New inquiry from the contact form.

                Name: %s
                Email: %s
                Company: %s
                Type: %s

                Message:
                %s
                """.formatted(
                message.getName(),
                message.getEmail(),
                message.getCompany() == null ? "-" : message.getCompany(),
                message.getInquiryType(),
                message.getMessage()));

        try {
            mailSender.send(mail);
        } catch (MailException e) {
            log.error("Could not send contact notification email for message id {}", message.getId(), e);
        }
    }
}