package com.domariev.emailsender.service;

import com.domariev.emailsender.model.NewsMailingRequest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface EmailService {

    MimeMessage sendEmail(NewsMailingRequest mailingRequest) throws MessagingException;
}
