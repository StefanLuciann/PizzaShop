package org.sda.com.pizzashop.service;

import javax.mail.MessagingException;

public interface MailService {

    void sendEmail(
            String from,
            String to,
            String subject,
            String content
    ) throws MessagingException;


}
