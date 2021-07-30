package com.domariev.emailsender.service;

import com.domariev.emailsender.model.NewsMailingRequest;

import javax.mail.MessagingException;
import java.util.List;

public interface NewsMailingRequestService {

    NewsMailingRequest add(NewsMailingRequest newsMailingRequest) throws MessagingException;

    List<NewsMailingRequest> findAll();

    NewsMailingRequest findById(String id);

    void delete(String id);
}
