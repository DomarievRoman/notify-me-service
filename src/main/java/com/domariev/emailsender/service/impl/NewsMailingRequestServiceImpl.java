package com.domariev.emailsender.service.impl;

import com.domariev.emailsender.model.NewsMailingRequest;
import com.domariev.emailsender.repository.NewsMailingRequestRepository;
import com.domariev.emailsender.service.NewsMailingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NewsMailingRequestServiceImpl implements NewsMailingRequestService {

    private final NewsMailingRequestRepository newsMailingRequestRepository;

    @Override
    public NewsMailingRequest add(NewsMailingRequest newsMailingRequest) {
        return newsMailingRequestRepository.insert(newsMailingRequest);
    }

    @Override
    public List<NewsMailingRequest> findAll() {
        List<NewsMailingRequest> mailingRequests = newsMailingRequestRepository.findAll();
        return mailingRequests;
    }

    @Override
    public NewsMailingRequest findById(String id) {
        NewsMailingRequest mailingRequest = newsMailingRequestRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No mailing founded with id " + id));
        return mailingRequest;
    }

    @Override
    public void delete(String id) {
        newsMailingRequestRepository.deleteById(id);
    }
}
