package com.domariev.emailsender.service.impl;

import com.domariev.emailsender.model.Email;
import com.domariev.emailsender.model.NewsMailingRequest;
import com.domariev.emailsender.service.EmailService;
import com.domariev.emailsender.service.MailingBodyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailingBodyService mailingBodyService;

    @Override
    public Email sendEmail(NewsMailingRequest mailingRequest) {
        Email email = new Email();
        email.setRecipient(mailingRequest.getEmail());
        email.setSubject("Notify Me Newsletter");
        email.setMailingBodyList(mailingBodyService.formBody(mailingRequest));
        return email;
    }
}
