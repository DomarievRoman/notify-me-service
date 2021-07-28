package com.domariev.emailsender.service;

import com.domariev.emailsender.model.MailingBody;
import com.domariev.emailsender.model.NewsMailingRequest;

import java.util.List;

public interface MailingBodyService {

    List<MailingBody> formBody(NewsMailingRequest newsMailingRequest);
}
