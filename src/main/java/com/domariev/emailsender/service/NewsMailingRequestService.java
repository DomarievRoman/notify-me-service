package com.domariev.emailsender.service;

import com.domariev.emailsender.model.NewsMailingRequest;

import java.util.List;

public interface NewsMailingRequestService {

    NewsMailingRequest add(NewsMailingRequest newsMailingRequest);

    List<NewsMailingRequest> findAll();

    NewsMailingRequest findById(String id);

    void delete(String id);
}
