package com.domariev.emailsender.service;

import com.domariev.emailsender.model.Email;
import com.domariev.emailsender.model.NewsMailingRequest;

public interface EmailService {

    Email sendEmail(NewsMailingRequest mailingRequest);
}
